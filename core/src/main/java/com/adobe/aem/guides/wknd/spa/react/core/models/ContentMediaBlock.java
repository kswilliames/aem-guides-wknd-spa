package com.adobe.aem.guides.wknd.spa.react.core.models;

import com.adobe.cq.wcm.core.components.models.Image;
import org.osgi.annotation.versioning.ProviderType;

@ProviderType
public interface ContentMediaBlock extends Image {
	/***
	 * The URL to populate the button as part of the block. The link should be based
	 * on the linkPath property that points to a page.
	 * 
	 * @return String URL
	 */
	public String getButtonUrl();

	/***
	 * The text to display on the button of the content media block.
	 * 
	 * @return String button text
	 */
	public String getButtonText();

	/**
	 * Return the title of the page specified by linkPath if `titleFromPage` is set
	 * to true. Otherwise return the value of `Heading`
	 * 
	 * @return
	 */
	public String getHeading();

	/**
	 * Return the pre-heading text for the block, if it exists
	 * 
	 * @return
	 */
	public String getPreHeading();

	/**
	 * Return the body text for the block, if it exists
	 * 
	 * @return
	 */
	public String getBody();

	/**
	 * Return the alignment for content of the block
	 * 
	 * @return
	 */
	public String getAlignment();

	/***
	 * @return a boolean if the component has enough content to display.
	 */
	public boolean isEmpty();
}