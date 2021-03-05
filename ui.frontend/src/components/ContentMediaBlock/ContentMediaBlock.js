import React from "react";
import { MapTo } from "@adobe/aem-react-editable-components";
import { Link } from "react-router-dom";
require("./ContentMediaBlock.scss");

const baseCss = "ContentMediaBlock";

export const ContentMediaBlockEditConfig = {
  emptyLabel: "Content Media Block",

  isEmpty: function (props) {
    return false;
  },
};

const Image = ({ src, alt, title }) =>
  !src?.trim().length < 1 ? (
    <div className={`${baseCss}__image-wrapper`}>
      <img
        className={`${baseCss}__image`}
        src={src}
        alt={alt}
        title={title ? title : alt}
      />
    </div>
  ) : (
    <div className={`${baseCss}__image-placeholder`} />
  );

const BodyContent = ({
  heading,
  preHeading,
  body,
  buttonUrl,
  buttonText,
  alignment,
}) => {
  // aligns the body content to the left if enabled
  const classAlignment =
    alignment === "left" ? ` ${baseCss}__content__align-left` : "";
  const className = `${baseCss}__content${classAlignment}`;
  return (
    <div className={className}>
      {preHeading && <h3 className={`${baseCss}__preheading`}>{preHeading}</h3>}
      {heading && <h2 className={`${baseCss}__heading`}>{heading}</h2>}
      {body && <p className={`${baseCss}__body`}>{body}</p>}
      {buttonUrl && <Button url={buttonUrl} text={buttonText} />}
    </div>
  );
};

const Button = ({ url, text }) => {
  if (url && text) {
    return (
      <div className={`${baseCss}__button-container`}>
        <Link to={url} title={text} className={`${baseCss}__button-link`}>
          {text}
        </Link>
      </div>
    );
  }

  return null;
};

const ContentMediaBlock = (props) => {
  const {
    buttonUrl,
    buttonText,
    preHeading,
    heading,
    body,
    alignment,
    ...imageProps
  } = props;
  return (
    <div className={baseCss}>
      {<Image {...imageProps} />}
      {
        <BodyContent
          buttonUrl={buttonUrl}
          buttonText={buttonText}
          preHeading={preHeading}
          heading={heading}
          body={body}
          alignment={alignment}
        />
      }
    </div>
  );
};

export default ContentMediaBlock;

MapTo("wknd-spa-react/components/content-media-block")(
  ContentMediaBlock,
  ContentMediaBlockEditConfig
);
