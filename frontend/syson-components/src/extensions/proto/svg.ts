/*******************************************************************************
 * Copyright (c) 2025 Obeo.
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/

import { svgNamespace } from './useProtoSvgExport.types';

export interface IElementVisitor {
  canHandle(element: Element): boolean;

  handle(element: Element, parentSvgElement: SVGElement, svgDocument: XMLDocument, viewport: DOMRect): void;
  // TODO: See if 'viewport' is really needed
}

const TEXT_NEW_LINE_SEPARATOR = '\n';
export class ListElementVisitor implements IElementVisitor {
  canHandle(element: Element): boolean {
    return (
      element.className.includes('listNode') &&
      element instanceof HTMLElement &&
      element.firstElementChild instanceof HTMLElement
    );
  }

  handle(element: Element, parentSvgElement: SVGElement, svgDocument: XMLDocument, _viewport: DOMRect) {
    if (element instanceof HTMLElement && element.firstElementChild instanceof HTMLElement) {
      const style = window.getComputedStyle(element.firstElementChild);
      const rect: SVGRectElement = svgDocument.createElementNS(svgNamespace, 'rect');
      const bounds = element.getBoundingClientRect();
      rect.setAttribute('width', String(bounds.width));
      rect.setAttribute('height', String(bounds.height));
      rect.setAttribute('x', String(bounds.left));
      rect.setAttribute('y', String(bounds.top));
      rect.setAttribute('fill', style.backgroundColor);
      rect.setAttribute('stroke', style.borderTopColor);
      rect.setAttribute('stroke-width', style.borderTopWidth);
      parentSvgElement.appendChild(rect);

      // TODO: do not use firstElementChild => we should add attributes (like data-type="label" or data-type="separator") to make it easier to find object to convert
      const labelElement = element.firstElementChild;
      const svglabelElements = this.handleLabel(labelElement, svgDocument, false);
      svglabelElements.forEach((svgElement) => {
        parentSvgElement.appendChild(svgElement);
      });
    }
  }

  private handleImage(image: HTMLImageElement, svgDocument: XMLDocument): SVGImageElement {
    // TODO: There is an issue with the image, `image.x` and `image.y` seem to be relative to the container not global, so they are stacking at the same position
    const svgImageElement: SVGImageElement = svgDocument.createElementNS('http://www.w3.org/2000/svg', 'image');
    svgImageElement.setAttribute('x', String(image.x));
    svgImageElement.setAttribute('y', String(image.y));
    svgImageElement.setAttribute('width', image.style.width);
    svgImageElement.setAttribute('height', image.style.height);
    svgImageElement.setAttribute('href', image.src);
    return svgImageElement;
  }

  private handleLabel(element: HTMLElement, svgDocument: XMLDocument, withIcon: boolean): SVGElement[] {
    const label: SVGElement[] = [];
    if (
      element.firstElementChild instanceof HTMLElement &&
      element.firstElementChild.firstElementChild instanceof HTMLElement
    ) {
      const labelElements = element.firstElementChild.firstElementChild.childNodes;
      let textElement: Text | null = null;
      if (labelElements.length === 2) {
        const imageDiv = labelElements[0]!;
        if (withIcon && imageDiv.firstChild instanceof HTMLImageElement) {
          const svgImage = this.handleImage(imageDiv.firstChild, svgDocument);
          label.push(svgImage);
        }

        const textDiv = labelElements[1]!;
        if (textDiv.firstChild instanceof Text) {
          textElement = textDiv.firstChild;
        }
      } else if (labelElements.length === 1) {
        // TODO: is it possible to have icon only?
        const textDiv = labelElements[0]!;
        if (textDiv.firstChild instanceof Text) {
          textElement = textDiv.firstChild;
        }
      }
      if (textElement) {
        const svgTextElement: SVGTextElement = this.handleTextElement(
          textElement,
          element.firstElementChild.firstElementChild,
          svgDocument
        );
        label.push(svgTextElement);
      }
    }
    if (
      element.childElementCount === 6 &&
      element.firstElementChild instanceof HTMLElement &&
      element.firstElementChild.nextElementSibling instanceof HTMLElement
    ) {
      const svgSeparator: SVGRectElement = this.handleSeparator(
        element.firstElementChild.nextElementSibling,
        svgDocument
      );
      label.push(svgSeparator);
    }
    return label;
  }

  private handleTextElement(textElement: Text, parentElement: HTMLElement, svgDocument: XMLDocument): SVGTextElement {
    const svgTextElement: SVGTextElement = svgDocument.createElementNS('http://www.w3.org/2000/svg', 'text');
    const styles = parentElement.style;
    // Copy text styles
    // https://css-tricks.com/svg-properties-and-css
    this.copyTextStyles(styles, svgTextElement);

    const tabSize = parseInt(styles.tabSize, 10);

    // Make sure the y attribute is the bottom of the box, not the baseline
    svgTextElement.setAttribute('dominant-baseline', 'text-after-edge');
    // TODO: Could it be possible the '\n' be '\r\n'?
    const lines = (textElement?.nodeValue ?? '').split(TEXT_NEW_LINE_SEPARATOR);
    if (lines.length === 1 && lines[0]) {
      // Juste le texte sans le tspan ???
      const lineRange: Range = textElement.ownerDocument.createRange();
      lineRange.setStart(textElement, 0);
      lineRange.setEnd(textElement, lines[0].length);
      let lineRectangle = lineRange.getBoundingClientRect();
      const textSpan = svgDocument.createElementNS(svgNamespace, 'tspan');
      // TODO: try with a value with many white-space, if it works, use white-space css property
      textSpan.setAttribute('xml:space', 'preserve');
      // SVG does not support tabs in text. Tabs get rendered as one space character. Convert the
      // tabs to spaces according to tab-size instead.
      // Ideally we would keep the tab and create offset tspans.
      // TODO: Test with a value containing a tab character (\t) with and without the replace by space
      textSpan.textContent = lines[0].replace(/\t/g, ' '.repeat(tabSize));
      textSpan.setAttribute('x', lineRectangle.x.toString());
      textSpan.setAttribute('y', lineRectangle.bottom.toString()); // intentionally bottom because of dominant-baseline setting
      textSpan.setAttribute('textLength', lineRectangle.width.toString());
      textSpan.setAttribute('lengthAdjust', 'spacingAndGlyphs');
      svgTextElement.append(textSpan);
    } else if (lines.length > 1) {
      let textIndex = 0;
      lines.forEach((lineValue) => {
        const lineRange: Range = textElement.ownerDocument.createRange();
        lineRange.setStart(textElement, textIndex);
        lineRange.setEnd(textElement, textIndex + lineValue.length);
        let lineRectangle = lineRange.getBoundingClientRect();
        const textSpan = svgDocument.createElementNS(svgNamespace, 'tspan');
        // TODO: try with a value with many white-space, if it works, use white-space css property
        textSpan.setAttribute('xml:space', 'preserve');
        // SVG does not support tabs in text. Tabs get rendered as one space character. Convert the
        // tabs to spaces according to tab-size instead.
        // Ideally we would keep the tab and create offset tspans.
        // TODO: Test with a value containing a tab character (\t) with and without the replace by space
        textSpan.textContent = lineValue.replace(/\t/g, ' '.repeat(tabSize));
        textSpan.setAttribute('x', lineRectangle.x.toString());
        textSpan.setAttribute('y', lineRectangle.bottom.toString()); // intentionally bottom because of dominant-baseline setting
        textSpan.setAttribute('textLength', lineRectangle.width.toString());
        textSpan.setAttribute('lengthAdjust', 'spacingAndGlyphs');
        svgTextElement.append(textSpan);
        textIndex = textIndex + lineValue.length + TEXT_NEW_LINE_SEPARATOR.length;
        // TODO: try with a text with ellipsis and see the difference in chrome and firefox
        // TODO: If there is an issue look for an hint in `@dom-to-svg$text.ts#handleTextNode (l.93)`
      });
    }
    return svgTextElement;
  }

  private handleSeparator(separator: HTMLElement, svgDocument: XMLDocument): SVGRectElement {
    const separatorBounds = separator.getBoundingClientRect();
    const separatorStyle = window.getComputedStyle(separator);
    const separatorSvg: SVGRectElement = svgDocument.createElementNS(svgNamespace, 'rect');
    separatorSvg.setAttribute('width', String(separatorBounds.width));
    separatorSvg.setAttribute('height', String(separatorBounds.height));
    separatorSvg.setAttribute('x', String(separatorBounds.left));
    separatorSvg.setAttribute('y', String(separatorBounds.top));
    separatorSvg.setAttribute('stroke', separatorStyle.borderBottomColor);
    separatorSvg.setAttribute('stroke-width', separatorStyle.borderBottomWidth);
    return separatorSvg;
  }

  private copyTextStyles(styles: CSSStyleDeclaration, svgElement: SVGElement): void {
    for (const textProperty of textAttributes) {
      const value = styles.getPropertyValue(textProperty);
      if (value) {
        svgElement.setAttribute(textProperty, value);
      }
    }
    // tspan uses fill, CSS uses color
    svgElement.setAttribute('fill', styles.color);
  }
}

const textAttributes = new Set([
  'color',
  'dominant-baseline',
  'font-family',
  'font-size',
  'font-size-adjust',
  'font-stretch',
  'font-style',
  'font-variant',
  'font-weight',
  'direction',
  'letter-spacing',
  'text-decoration',
  'text-anchor',
  'text-decoration',
  'text-rendering',
  'unicode-bidi',
  'word-spacing',
  'writing-mode',
  'user-select',
] as const);
