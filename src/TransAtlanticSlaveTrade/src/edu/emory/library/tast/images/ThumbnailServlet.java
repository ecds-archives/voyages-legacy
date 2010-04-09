/*
Copyright 2010 Emory University
	
	    This file is part of Trans-Atlantic Slave Voyages.
	
	    Trans-Atlantic Slave Voyages is free software: you can redistribute it and/or modify
	    it under the terms of the GNU General Public License as published by
	    the Free Software Foundation, either version 3 of the License, or
	    (at your option) any later version.
	
	    Trans-Atlantic Slave Voyages is distributed in the hope that it will be useful,
	    but WITHOUT ANY WARRANTY; without even the implied warranty of
	    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	    GNU General Public License for more details.
	
	    You should have received a copy of the GNU General Public License
	    along with Trans-Atlantic Slave Voyages.  If not, see <http://www.gnu.org/licenses/>. 
*/
package edu.emory.library.tast.images;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.File;
import java.io.IOException;

import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;

import edu.emory.library.tast.AppConfig;
import edu.emory.library.tast.util.StringUtils;

/**
 * Servlet which provides data for thumbnail images.
 *
 */
public class ThumbnailServlet extends HttpServlet
{
	
	private static final long serialVersionUID = 50767109010059686L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		
		// location of images
		String baseUrl = AppConfig.getConfiguration().getString(AppConfig.IMAGES_URL);
		baseUrl = StringUtils.trimEnd(baseUrl, '/');
		
		// image name and size
		String imageFileName = request.getParameter("i");
		int thumbnailWidth = Integer.parseInt(request.getParameter("w"));
		int thumbnailHeight = Integer.parseInt(request.getParameter("h"));

		// image dir
		String imagesDir = AppConfig.getConfiguration().getString(AppConfig.IMAGES_DIRECTORY);

		// create the thumbnail name
		String thumbnailFileName =
				FilenameUtils.getBaseName(imageFileName) + "-" +
				thumbnailWidth + "x" + thumbnailHeight + ".png";
		
		// does it exist?
		File thumbnailFile = new File(imagesDir, thumbnailFileName);
		if (thumbnailFile.exists())
		{
			response.sendRedirect(baseUrl + "/" + thumbnailFileName);
			return;
		}

		// read the image
		File imageFile = new File(imagesDir, imageFileName);
		BufferedImage image = ImageIO.read(imageFile);
		int imageWidth = image.getWidth();
		int imageHeight = image.getHeight();
		BufferedImage imageCopy = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
		imageCopy.getGraphics().drawImage(image, 0, 0, imageWidth, imageHeight, 0, 0, imageWidth, imageHeight, null);

		// height is calculated automatically
		if (thumbnailHeight == 0)
			thumbnailHeight = (int) ((double)thumbnailWidth / (double)imageWidth * (double)imageHeight);  
		
		// width is calculated automatically
		if (thumbnailWidth == 0)
			thumbnailWidth = (int) ((double)thumbnailHeight / (double)imageHeight * (double)imageWidth);  

		// create an empty thumbnail
		BufferedImage thumbnail = new BufferedImage(thumbnailWidth, thumbnailHeight, BufferedImage.TYPE_INT_RGB);
		Graphics2D gr = thumbnail.createGraphics();
		gr.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		gr.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

		// determine the piece of the image which we want to clip
		int clipX1, clipX2, clipY1, clipY2; 
		if (imageWidth * thumbnailHeight >  thumbnailWidth * imageHeight)
		{
			
			int clipWidth = (int) Math.round(((double) thumbnailWidth / (double) thumbnailHeight) * (double) imageHeight); 
			int imgCenterX = imageWidth / 2; 
			clipX1 = imgCenterX - clipWidth / 2; 
			clipX2 = imgCenterX + clipWidth / 2; 
			clipY1 = 0;
			clipY2 = imageHeight; 
		}
		else
		{
			int clipHeight = (int) Math.round(((double) thumbnailHeight / (double) thumbnailWidth) * (double) imageWidth);
			int imgCenterY = imageHeight / 2; 
			clipX1 = 0; 
			clipX2 = imageWidth; 
			clipY1 = imgCenterY - clipHeight / 2;
			clipY2 = imgCenterY + clipHeight / 2;
		}
		
		// we filter the image first to get better results when shrinking
		if (2*thumbnailWidth < clipX2 - clipX1 || 2*thumbnailHeight < clipY2 - clipY1)
		{
			
			int kernelDimX = (clipX2 - clipX1) / (thumbnailWidth);
			int kernelDimY = (clipY2 - clipY1) / (thumbnailHeight);
			
			if (kernelDimX % 2 == 0) kernelDimX++;
			if (kernelDimY % 2 == 0) kernelDimY++;
			
			if (kernelDimX < kernelDimY) kernelDimX = kernelDimY;
			if (kernelDimY < kernelDimX) kernelDimY = kernelDimX;
			
			float[] blurKernel = new float[kernelDimX * kernelDimY];
			for (int i = 0; i < kernelDimX; i++)
				for (int j = 0; j < kernelDimY; j++)
					blurKernel[i * kernelDimX + j] = 1.0f/(float)(kernelDimX * kernelDimY); 
			
			BufferedImageOp op = new ConvolveOp(new Kernel(kernelDimX, kernelDimY, blurKernel));
			imageCopy = op.filter(imageCopy, null);
			
		}
		
		// draw the thumbnail
		gr.drawImage(imageCopy,
				0, 0, thumbnailWidth, thumbnailHeight,
				clipX1, clipY1, clipX2, clipY2, null);
		
		// and we are done
		gr.dispose();
		ImageIO.write(thumbnail, "png", thumbnailFile);
		
		// redirect to it
		response.sendRedirect(baseUrl + "/" + thumbnailFileName);

	}
	
	public static String createThumbnailUrl(String imageName, int width, int height)
	{
		return createThumbnailUrl(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath(), imageName, width, height);
	}

	public static String createThumbnailUrl(FacesContext context, String imageName, int width, int height)
	{
		return createThumbnailUrl(context.getExternalContext().getRequestContextPath(), imageName, width, height);
	}

	public static String createThumbnailUrl(String contextPath, String imageName, int width, int height)
	{
		StringBuffer buff = new StringBuffer();
		appendThumbnailUrl(buff, contextPath, imageName, width, height);
		return buff.toString();
	}

	public static StringBuffer appendThumbnailUrl(StringBuffer buff, String contextPath, String imageName, int width, int height)
	{
		buff.append(contextPath);
		buff.append("/servlet/thumbnail");
		buff.append("?i=").append(imageName);
		buff.append("&w=").append(width);
		buff.append("&h=").append(height);
		return buff;
	}

}
