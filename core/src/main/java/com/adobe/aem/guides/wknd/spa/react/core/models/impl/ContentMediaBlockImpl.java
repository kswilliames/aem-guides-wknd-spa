package com.adobe.aem.guides.wknd.spa.react.core.models.impl;

// allows us to call methods when the model is initialised
import javax.annotation.PostConstruct;

// references the interface/contract for the Sling model
import com.adobe.aem.guides.wknd.spa.react.core.models.ContentMediaBlock;

// allows us to map properties from the Sling resourceSuperType (in this case, the Image model)
import com.adobe.cq.wcm.core.components.models.Image;
import org.apache.sling.models.factory.ModelFactory;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;

// allows us to reference and manage pages
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;

// used to process and check strings
import org.apache.commons.lang3.StringUtils;

// allows us to add our class as a Sling model
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import org.apache.sling.models.annotations.Exporter;

// allows injecting the adapatable object SlingHttpServletRequest into our model
import org.apache.sling.models.annotations.injectorspecific.Self;

// allows us to map properties from the JCR content node to the Sling model
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

// turns class into a Sling model
@Model(adaptables = SlingHttpServletRequest.class, adapters = { ContentMediaBlock.class,
		ComponentExporter.class }, resourceType = ContentMediaBlockImpl.RESOURCE_TYPE, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
@Exporter(name = ExporterConstants.SLING_MODEL_EXPORTER_NAME, extensions = ExporterConstants.SLING_MODEL_EXTENSION)
public class ContentMediaBlockImpl implements ContentMediaBlock {
	protected static final String RESOURCE_TYPE = "wknd-spa-react/components/content-media-block";

	@Self
	private SlingHttpServletRequest request;

	@OSGiService
	private ModelFactory modelFactory;

	@ValueMapValue
	private String heading;

	@ValueMapValue
	private String preHeading;

	@ValueMapValue
	private String body;

	@ValueMapValue
	private String alignment;

	@ValueMapValue
	private String linkPath;

	@ValueMapValue
	private String buttonText;

	@ValueMapValue
	private boolean headingFromPage;

	private Image image;

	/***
	 * Made available via
	 * https://docs.adobe.com/content/help/en/experience-manager-htl/using/htl/global-objects.html#java-backed-objects
	 */
	@ScriptVariable
	PageManager pageManager;

	/***
	 * The underlying page linked by the content media block
	 */
	private Page linkPage;

	@PostConstruct
	private void init() {
		if (StringUtils.isNotBlank(linkPath) && pageManager != null) {
			linkPage = pageManager.getPage(this.linkPath);
		}

		image = modelFactory.getModelFromWrappedRequest(request, request.getResource(), Image.class);
	}

	@Override
	public String getSrc() {
		return null != image ? image.getSrc() : null;
	}

	@Override
	public String getAlt() {
		return null != image ? image.getAlt() : null;
	}

	@Override
	public String getExportedType() {
		return ContentMediaBlockImpl.RESOURCE_TYPE;
	}

	@Override
	public String getButtonUrl() {
		if (linkPage != null) {
			return linkPage.getPath() + ".html";
		}
		return null;
	}

	@Override
	public String getButtonText() {
		if (buttonText != null) {
			return buttonText;
		}
		return "Read more...";
	}

	@Override
	public String getHeading() {
		if (headingFromPage) {
			return linkPage != null ? linkPage.getTitle() : null;
		}
		return heading;
	}

	@Override
	public String getPreHeading() {
		return preHeading;
	}

	@Override
	public String getBody() {
		return body;
	}

	@Override
	public String getAlignment() {
		if (alignment == null)
			return "right";
		return alignment;
	}

	@Override
	public boolean isEmpty() {
		final Image componentImage = getImage();

		if (!headingFromPage && StringUtils.isBlank(heading)) {
			// heading is missing, but required because headingFromPage is not enabled
			return true;
		} else if (headingFromPage && (linkPage == null || StringUtils.isBlank(linkPage.getTitle()))) {
			// linkPage title is missing, but required because headingFromPage is enabled
			return true;
		} else if (StringUtils.isBlank(body)) {
			// body is missing, but required
			return true;
		} else if (componentImage == null || StringUtils.isBlank(componentImage.getSrc())) {
			// A valid image is required
			return true;
		} else {
			// Everything is populated, so this component is not considered empty
			return false;
		}
	}

	/**
	 * @return the Image Sling Model of this resource, or null if the resource
	 *         cannot create a valid Image Sling Model.
	 */
	private Image getImage() {
		return image;
	}

}