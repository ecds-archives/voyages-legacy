package edu.emory.library.tast.ui.images;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;

import edu.emory.library.tast.AppConfig;

public class ThumbnailServlet extends HttpServlet
{
	
	private static final long serialVersionUID = 50767109010059686L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		
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
			response.sendRedirect(request.getContextPath() + "/images/" + thumbnailFileName);
			return;
		}

		// read the image
		File imageFile = new File(imagesDir, imageFileName);
		BufferedImage image = ImageIO.read(imageFile);
		int imageWidth = image.getWidth();
		int imageHeight = image.getHeight();
		
		// create an empty thumbnail
		BufferedImage thumbnail = new BufferedImage(thumbnailWidth, thumbnailHeight, BufferedImage.TYPE_INT_RGB);
		Graphics2D gr = thumbnail.createGraphics();
		gr.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

		// determine the piece of the image which we want to clip
		int clipX1, clipX2, clipY1, clipY2; 
		if (imageWidth * thumbnailHeight >  thumbnailWidth * imageHeight)
		{
			
			int clipWidth = (int) Math.round(((double) thumbnailWidth / (double) thumbnailHeight) * (double) imageHeight); 
			clipX1 = (imageWidth - clipWidth) / 2; 
			clipY1 = clipWidth + (imageWidth - clipWidth) / 2;
			clipX2 = 0; 
			clipY2 = imageHeight - 1; 
		}
		else
		{
			int clipHeight = (int) Math.round(((double) thumbnailHeight / (double) thumbnailWidth) * (double) imageWidth); 
			clipX1 = 0; 
			clipY1 = imageWidth - 1; 
			clipX2 = (imageWidth - clipHeight) / 2; 
			clipY2 = clipHeight + (imageWidth - clipHeight) / 2;
		}

		// draw the thumbnail
		gr.drawImage(image,
				0, 0, thumbnailWidth, thumbnailHeight,
				clipX1, clipX2, clipY1, clipY2, null);
		
		// and we are done
		gr.dispose();
		ImageIO.write(thumbnail, "png", thumbnailFile);
		
		// redirect to it
		response.sendRedirect(request.getContextPath() + "/images/" + thumbnailFileName);

	}

}
