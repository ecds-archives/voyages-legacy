package edu.emory.library.tast.database.stat.charts;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;
import java.util.List;

import org.jfree.chart.axis.AxisState;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPosition;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.CategoryLabelWidthType;
import org.jfree.chart.axis.CategoryTick;
import org.jfree.chart.axis.Tick;
import org.jfree.chart.entity.EntityCollection;
import org.jfree.chart.entity.TickLabelEntity;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.text.TextBlock;
import org.jfree.ui.RectangleAnchor;
import org.jfree.ui.RectangleEdge;

/**
 * Subclass for CategoryAxis that automatically hides some category labels (if there is too many labels).
 * @author Pawel Jurczyk
 *
 */
public class SkippableCategoryAxis extends CategoryAxis {

	private static final long serialVersionUID = -5591468602200139851L;

	// private double lowerMargin;
	// private double upperMargin;
	// private double categoryMargin;
	// private int maximumCategoryLabelLines;
	// private float maximumCategoryLabelWidthRatio;
	// private CategoryLabelPositions categoryLabelPositions;
	// private int categoryLabelPositionOffset;
	// private HashMap categoryLabelToolTips;

	/**
	 * Constructor.
	 */
	public SkippableCategoryAxis(CategoryAxis ca) {
		super.setLowerMargin(ca.getLowerMargin());
		super.setUpperMargin(ca.getUpperMargin());
		super.setCategoryMargin(ca.getCategoryMargin());
		super.setMaximumCategoryLabelLines(ca.getMaximumCategoryLabelLines());
		super.setMaximumCategoryLabelWidthRatio(ca.getMaximumCategoryLabelWidthRatio());

		setTickMarksVisible(false);

		super.setCategoryLabelPositionOffset(ca.getCategoryLabelPositionOffset());
		super.setCategoryLabelPositions(ca.getCategoryLabelPositions());
		//this.categoryLabelToolTips = new HashMap();
	}

	/**
	 * Draws the category labels and returns the updated axis state.
	 * 
	 * @param g2
	 *            the graphics device (<code>null</code> not permitted).
	 * @param dataArea
	 *            the area inside the axes (<code>null</code> not permitted).
	 * @param edge
	 *            the axis location (<code>null</code> not permitted).
	 * @param state
	 *            the axis state (<code>null</code> not permitted).
	 * @param plotState
	 *            collects information about the plot (<code>null</code>
	 *            permitted).
	 * 
	 * @return The updated axis state (never <code>null</code>).
	 */
	protected AxisState drawCategoryLabels(Graphics2D g2, Rectangle2D dataArea, RectangleEdge edge, AxisState state,
			PlotRenderingInfo plotState) {

		if (state == null) {
			throw new IllegalArgumentException("Null 'state' argument.");
		}

		if (isTickLabelsVisible()) {
			List ticks = refreshTicks(g2, state, dataArea, edge);
			state.setTicks(ticks);

			int categoryIndex = 0;
			Iterator iterator = ticks.iterator();
			while (iterator.hasNext()) {

				CategoryTick tick = (CategoryTick) iterator.next();
				g2.setFont(getTickLabelFont(tick.getCategory()));
				g2.setPaint(getTickLabelPaint(tick.getCategory()));

				CategoryLabelPosition position = getCategoryLabelPositions().getLabelPosition(edge);
				double x0 = 0.0;
				double x1 = 0.0;
				double y0 = 0.0;
				double y1 = 0.0;
				if (edge == RectangleEdge.TOP) {
					x0 = getCategoryStart(categoryIndex, ticks.size(), dataArea, edge);
					x1 = getCategoryEnd(categoryIndex, ticks.size(), dataArea, edge);
					y1 = state.getCursor() - getCategoryLabelPositionOffset();
					y0 = y1 - state.getMax();
				} else if (edge == RectangleEdge.BOTTOM) {
					x0 = getCategoryStart(categoryIndex, ticks.size(), dataArea, edge);
					x1 = getCategoryEnd(categoryIndex, ticks.size(), dataArea, edge);
					y0 = state.getCursor() + getCategoryLabelPositionOffset();
					y1 = y0 + state.getMax();
				} else if (edge == RectangleEdge.LEFT) {
					y0 = getCategoryStart(categoryIndex, ticks.size(), dataArea, edge);
					y1 = getCategoryEnd(categoryIndex, ticks.size(), dataArea, edge);
					x1 = state.getCursor() - getCategoryLabelPositionOffset();
					x0 = x1 - state.getMax();
				} else if (edge == RectangleEdge.RIGHT) {
					y0 = getCategoryStart(categoryIndex, ticks.size(), dataArea, edge);
					y1 = getCategoryEnd(categoryIndex, ticks.size(), dataArea, edge);
					x0 = state.getCursor() + getCategoryLabelPositionOffset();
					x1 = x0 - state.getMax();
				}
				Rectangle2D area = new Rectangle2D.Double(x0, y0, (x1 - x0), (y1 - y0));
				Point2D anchorPoint = RectangleAnchor.coordinates(area, position.getCategoryAnchor());
				TextBlock block = tick.getLabel();
				block.draw(g2, (float) anchorPoint.getX(), (float) anchorPoint.getY(), position.getLabelAnchor(),
						(float) anchorPoint.getX(), (float) anchorPoint.getY(), position.getAngle());
				Shape bounds = block.calculateBounds(g2, (float) anchorPoint.getX(), (float) anchorPoint.getY(),
						position.getLabelAnchor(), (float) anchorPoint.getX(), (float) anchorPoint.getY(), position
								.getAngle());
				if (plotState != null && plotState.getOwner() != null) {
					EntityCollection entities = plotState.getOwner().getEntityCollection();
					if (entities != null) {
						String tooltip = getCategoryLabelToolTip(tick.getCategory());
						entities.add(new TickLabelEntity(bounds, tooltip, null));
					}
				}
				categoryIndex++;
			}

			if (edge.equals(RectangleEdge.TOP)) {
				double h = state.getMax();
				state.cursorUp(h);
			} else if (edge.equals(RectangleEdge.BOTTOM)) {
				double h = state.getMax();
				state.cursorDown(h);
			} else if (edge == RectangleEdge.LEFT) {
				double w = state.getMax();
				state.cursorLeft(w);
			} else if (edge == RectangleEdge.RIGHT) {
				double w = state.getMax();
				state.cursorRight(w);
			}
		}
		return state;
	}

	public List refreshTicks(Graphics2D g2, AxisState state, Rectangle2D dataArea, RectangleEdge edge) {

		List ticks = new java.util.ArrayList();

		// sanity check for data area...
		if (dataArea.getHeight() <= 0.0 || dataArea.getWidth() < 0.0) {
			return ticks;
		}

		CategoryPlot plot = (CategoryPlot) getPlot();
		List categories = plot.getCategories();
		double max = 0.0;
		float maxWidth = (float) (dataArea.getWidth() / categories.size() * 0.9f);
		
		if (categories != null) {
			
			CategoryLabelPosition position = getCategoryLabelPositions().getLabelPosition(edge);
			float r = getMaximumCategoryLabelWidthRatio();
			if (r <= 0.0) {
				r = position.getWidthRatio();
			}

			float l = 0.0f;
			if (position.getWidthType() == CategoryLabelWidthType.CATEGORY) {
				l = (float) calculateCategorySize(categories.size(), dataArea, edge);
			} else {
				if (RectangleEdge.isLeftOrRight(edge)) {
					l = (float) dataArea.getWidth();
				} else {
					l = (float) dataArea.getHeight();
				}
			}
			int categoryIndex = 0;
			int categorySkip = 0;
			Iterator iterator = categories.iterator();
			
			while (iterator.hasNext()) {
				
				if (categorySkip != 0) {
					 categorySkip--;
					 categoryIndex++;
					 iterator.next();
					 continue;
				}
				
				Comparable category = (Comparable) iterator.next();
				Font font = this.getTickLabelFont();
				FontRenderContext frc = g2.getFontRenderContext();
				String labelS = category.toString();
				Rectangle2D labelBounds = font.getStringBounds(labelS, frc);
				double d;
				if (getCategoryLabelPositions().equals(CategoryLabelPositions.DOWN_90) || 
						getCategoryLabelPositions().equals(CategoryLabelPositions.UP_90)) {
					d = (float)labelBounds.getHeight();
				}
				else if (getCategoryLabelPositions().equals(CategoryLabelPositions.DOWN_45) || 
							getCategoryLabelPositions().equals(CategoryLabelPositions.UP_45)) {
					d = (float)labelBounds.getHeight();
				} else {
					d = (float)labelBounds.getWidth();
				}
				TextBlock label = createLabel(category, l * r, edge, g2);
				if (edge == RectangleEdge.TOP || edge == RectangleEdge.BOTTOM) {
					max = Math.max(max, calculateTextBlockHeight(label, position, g2));
				} else if (edge == RectangleEdge.LEFT || edge == RectangleEdge.RIGHT) {
					max = Math.max(max, calculateTextBlockWidth(label, position, g2));
				}
				
				Tick tick = new CategoryTick(category, label, position.getLabelAnchor(), position.getRotationAnchor(),
						position.getAngle());
				ticks.add(tick);
				categoryIndex = categoryIndex + 1;
				categorySkip = (int) ((d - maxWidth / 2) / maxWidth);
				if (categorySkip > 0) {
					categorySkip += 2;
				}
			}
		}
		state.setMax(max);
		return ticks;

	}

}
