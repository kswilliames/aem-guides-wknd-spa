package com.adobe.aem.guides.wknd.spa.react.core.models.impl;

// Jupiter test utilities
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

// Mockito mock utilities
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.Mock;

import static org.mockito.Mockito.*;

// accesses modelFactory OSGi service to allow us to Mock some Sing model methods/attributes, like providing images
import org.apache.sling.models.factory.ModelFactory;
import org.apache.sling.api.resource.Resource;

// needed for setting up AEM context for stubbing JCR/Sling/OSGi/AEM APIs
import org.junit.jupiter.api.extension.ExtendWith;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

// imports model to be tested
import com.adobe.aem.guides.wknd.spa.react.core.models.ContentMediaBlock;
import com.adobe.cq.wcm.core.components.models.Image;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.day.cq.wcm.api.WCMException;

// extends test with AEM context and Mockito mocks
@ExtendWith({ AemContextExtension.class, MockitoExtension.class })
public class ContentMediaBlockImplTest {

	// needed to stub/mock the AEM context
	private final AemContext ctx = new AemContext();

	// mocks image object
	@Mock
	private Image image;

	// mocks modelFactory to inject attributes into the model
	@Mock
	private ModelFactory modelFactory;

	// mocks page object
	@Mock
	private Page page;

	// mocks pageManager object
	@Mock
	private PageManager pageManager;

	private final String pageParentPath = "/content/";
	private final String pagePageName = "the-mountain";
	private final String pageTemplate = "/apps/wknd-spa-react/templates/page-content";
	private final String pageTitle = "The Mountain";

	// sets up each test
	@BeforeEach
	void setUp() throws Exception {
		// registers the Sling model we are testing
		ctx.addModelsForClasses(ContentMediaBlockImpl.class, Image.class);
		// loads mock resource structures into the mock context
		ctx.load().json("/com/adobe/aem/guides/wknd/spa/react/core/models/impl/ContentMediaBlockImplTest.json",
				"/content");

		// returns image
		lenient().when(modelFactory.getModelFromWrappedRequest(eq(ctx.request()), any(Resource.class), eq(Image.class)))
				.thenReturn(image);

		// inject our mocked page
		ctx.pageManager().create(pageParentPath, pagePageName, pageTemplate, pageTitle);

		// register OSGi service with AEM Context to provide mocked modelFactory service
		ctx.registerService(ModelFactory.class, modelFactory, org.osgi.framework.Constants.SERVICE_RANKING,
				Integer.MAX_VALUE);
	}

	@Test
	void testGetButtonUrl() {
		final String path = pageParentPath + pagePageName;
		final String expected = path + ".html";

		ctx.currentResource("/content/content-media-block");

		ContentMediaBlock contentMediaBlock = ctx.request().adaptTo(ContentMediaBlock.class);

		String actual = contentMediaBlock.getButtonUrl();

		assertEquals(expected, actual);
	}

	@Test
	void testGetButtonText() {
		final String expected = "Find out more...";

		ctx.currentResource("/content/content-media-block");
		ContentMediaBlock contentMediaBlock = ctx.request().adaptTo(ContentMediaBlock.class);

		String actual = contentMediaBlock.getButtonText();

		assertEquals(expected, actual);
	}

	@Test
	void testGetHeading() {
		final String expected = "Captain Kirk is climbing a mountain";

		ctx.currentResource("/content/content-media-block");
		ContentMediaBlock contentMediaBlock = ctx.request().adaptTo(ContentMediaBlock.class);

		String actual = contentMediaBlock.getHeading();

		assertEquals(expected, actual);
	}

	@Test
	void testGetHeading_withTitle() {
		final String expected = pageTitle;

		ctx.currentResource("/content/without-page-title");
		ContentMediaBlock contentMediaBlock = ctx.request().adaptTo(ContentMediaBlock.class);

		String actual = contentMediaBlock.getHeading();

		assertEquals(expected, actual);
	}

	@Test
	void testGetPreHeading() {
		final String expected = "Why is he climbing that mountain?";

		ctx.currentResource("/content/content-media-block");
		ContentMediaBlock contentMediaBlock = ctx.request().adaptTo(ContentMediaBlock.class);

		String actual = contentMediaBlock.getPreHeading();

		assertEquals(expected, actual);
	}

	@Test
	void testGetBody() {
		final String expected = "Is it because he's in love?";

		ctx.currentResource("/content/content-media-block");
		ContentMediaBlock contentMediaBlock = ctx.request().adaptTo(ContentMediaBlock.class);

		String actual = contentMediaBlock.getBody();

		assertEquals(expected, actual);
	}

	@Test
	void testGetAlignment() {
		final String expected = "left";

		ctx.currentResource("/content/content-media-block");
		ContentMediaBlock contentMediaBlock = ctx.request().adaptTo(ContentMediaBlock.class);

		String actual = contentMediaBlock.getAlignment();

		assertEquals(expected, actual);
	}

	@Test
	public void testIsEmpty() {
		ctx.currentResource("/content/empty");

		ContentMediaBlock contentMediaBlock = ctx.request().adaptTo(ContentMediaBlock.class);

		assertTrue(contentMediaBlock.isEmpty());
	}

	@Test
	public void testIsEmpty_WithoutHeading() {
		ctx.currentResource("/content/without-heading");

		ContentMediaBlock contentMediaBlock = ctx.request().adaptTo(ContentMediaBlock.class);

		assertTrue(contentMediaBlock.isEmpty());
	}

	@Test
	public void testIsEmpty_WithoutPageTitle() throws WCMException {
		ctx.currentResource("/content/without-page-title");

		// returns null for Page on mocked model to simulate missing linkedPage
		pageManager.delete(page, false);

		ContentMediaBlock contentMediaBlock = ctx.request().adaptTo(ContentMediaBlock.class);

		assertTrue(contentMediaBlock.isEmpty());
	}

	@Test
	public void testIsEmpty_WithoutBody() {
		ctx.currentResource("/content/without-body");

		ContentMediaBlock contentMediaBlock = ctx.request().adaptTo(ContentMediaBlock.class);

		assertTrue(contentMediaBlock.isEmpty());
	}

	@Test
	public void testIsEmpty_WithoutImage() {
		ctx.currentResource("/content/without-body");

		// returns null for Image on mocked model
		lenient().when(modelFactory.getModelFromWrappedRequest(eq(ctx.request()), any(Resource.class), eq(Image.class)))
				.thenReturn(null);

		ContentMediaBlock contentMediaBlock = ctx.request().adaptTo(ContentMediaBlock.class);

		assertTrue(contentMediaBlock.isEmpty());
	}

	@Test
	public void testIsEmpty_WithoutImageSrc() {
		ctx.currentResource("/content/content-media-block");

		// simulates missing imageSrc url
		lenient().when(image.getSrc()).thenReturn("");

		ContentMediaBlock contentMediaBlock = ctx.request().adaptTo(ContentMediaBlock.class);

		assertTrue(contentMediaBlock.isEmpty());
	}

	@Test
	public void testIsNotEmpty() throws NoSuchFieldException, SecurityException {
		ctx.currentResource("/content/content-media-block");

		when(image.getSrc()).thenReturn("/content/mountain.png");

		ContentMediaBlock contentMediaBlock = ctx.request().adaptTo(ContentMediaBlock.class);

		assertFalse(contentMediaBlock.isEmpty());
	}
}