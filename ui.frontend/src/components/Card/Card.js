import React from "react";
import Image from "../Image/Image";
import { MapTo } from "@adobe/aem-react-editable-components";
import { Link } from "react-router-dom";
require("./Card.scss");

export const CardEditConfig = {
  emptyLabel: "Card",

  isEmpty: function (props) {
    return !props || !props.src || props.src.trim().length < 1;
  },
};

const getLastModifiedDisplayDate = (cardLastModified) => {
  const lastModifiedDate = cardLastModified ? new Date(cardLastModified) : null;

  if (lastModifiedDate) {
    return lastModifiedDate.toLocaleDateString();
  }
  return null;
};

const ImageContent = (props) => (
  <div className="Card__image">
    <Image {...props} />
  </div>
);

const BodyContent = ({
  cardTitle,
  lastModifiedDisplayDate,
  ctaLinkURL,
  ctaText,
  title,
}) => (
  <div class="Card__content">
    <h2 class="Card__title">
      {" "}
      {cardTitle}
      <span class="Card__lastmod">{lastModifiedDisplayDate}</span>
    </h2>
    {<CtaButton ctaLinkURL={ctaLinkURL} ctaText={ctaText} title={title} />}
  </div>
);

const CtaButton = ({ ctaLinkURL, ctaText, title }) => {
  if (ctaLinkURL && ctaText) {
    return (
      <div className="Card__action-container">
        <Link to={ctaLinkURL} title={title} className="Card__action-link">
          {ctaText}
        </Link>
      </div>
    );
  }

  return null;
};

const Card = (props) => {
  const { cardLastModified, ctaLinkURL, ctaText, title } = props;
  if (CardEditConfig.isEmpty(props)) {
    return null;
  }

  const lastModifiedDisplayDate = getLastModifiedDisplayDate(cardLastModified);

  return (
    <div className="Card">
      {<ImageContent {...props} />}
      {
        <BodyContent
          ctaLinkURL={ctaLinkURL}
          ctaText={ctaText}
          title={title}
          lastModifiedDisplayDate={lastModifiedDisplayDate}
        />
      }
    </div>
  );
};

export default Card;

MapTo("wknd-spa-react/components/card")(Card, CardEditConfig);
