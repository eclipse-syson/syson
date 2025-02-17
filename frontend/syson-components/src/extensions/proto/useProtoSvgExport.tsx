/*******************************************************************************
 * Copyright (c) 2024 Obeo.
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

import { EdgeData, NodeData } from '@eclipse-sirius/sirius-components-diagrams';
import {
  Edge,
  getNodesBounds,
  getViewportForBounds,
  Node as ReactFlowNode,
  Rect,
  useReactFlow,
  Viewport,
} from '@xyflow/react';
import { toSvg } from 'html-to-image';
import { useCallback, useEffect, useRef } from 'react';
import { createRoot } from 'react-dom/client';
import { IElementVisitor, ListElementVisitor } from './svg';
import { createSvg } from './SvgElement';
import { UseProtoSvgExport } from './useProtoSvgExport.types';

const defaultSVGElementVisitor: IElementVisitor[] = [new ListElementVisitor()];

function createImage(url: string): Promise<HTMLImageElement> {
  return new Promise((resolve, reject) => {
    const img = new Image();
    img.onload = () => {
      img.decode().then(() => {
        requestAnimationFrame(() => resolve(img));
      });
    };
    img.onerror = reject;
    img.crossOrigin = 'anonymous';
    img.decoding = 'async';
    img.src = url;
  });
}

export const useProtoSvgExport = (): UseProtoSvgExport => {
  const reactFlow = useReactFlow<ReactFlowNode<NodeData>, Edge<EdgeData>>();

  const exportSvg = useCallback((callback: (result: string, expected: string) => void) => {
    const nodesBounds: Rect = getNodesBounds(reactFlow.getNodes());
    const imageWidth: number = nodesBounds.width;
    const imageHeight: number = nodesBounds.height;

    const viewport: Viewport = getViewportForBounds(nodesBounds, imageWidth, imageHeight, 0.5, 2, 0.05);
    const edges: HTMLElement | null = document.querySelector<HTMLElement>('.react-flow__edges');
    const edgeMarkers: HTMLElement | null = document.getElementById('edge-markers');
    debugger;
    const reactFlowViewportContainer: HTMLElement | null = document.querySelector<HTMLElement>('.react-flow__viewport');
    if (reactFlowViewportContainer && edges && edgeMarkers) {
      const clonedEdgeMarkers: Node = edgeMarkers.cloneNode(true);
      edges.insertBefore(clonedEdgeMarkers, edges.firstChild);

      const cssTransform = `translate(${viewport.x}px, ${viewport.y}px) scale(${viewport.zoom})`;
      toSvg(reactFlowViewportContainer, {
        backgroundColor: '#ffffff',
        width: imageWidth,
        height: imageHeight,
        style: {
          width: imageWidth.toString(),
          height: imageHeight.toString(),
          transform: cssTransform,
        },
      })
        .then(createImage)
        .then((image) => {
          const textSvg = decodeURIComponent(image.src.replace('data:image/svg+xml;charset=utf-8,', ''));
          const parser = new DOMParser();
          const doc = parser.parseFromString(textSvg, 'image/svg+xml');
          debugger;
          const otherViewport = doc.querySelector<HTMLElement>('.react-flow__viewport');
          const style = otherViewport?.querySelector<SVGStyleElement>('style');
          // TODO: no need for otherViewport
          if (otherViewport && style) {
            debugger;
            const hiddenContainer: HTMLDivElement = document.createElement('div');
            hiddenContainer.id = 'hidden-container';
            hiddenContainer.style.display = 'inline-block';
            hiddenContainer.style.position = 'absolute';
            hiddenContainer.style.top = '0';
            hiddenContainer.style.visibility = 'hidden';
            hiddenContainer.style.zIndex = '-1';
            document.body.appendChild(hiddenContainer);

            const Element = () => {
              const divRef = useRef<HTMLDivElement | null>(null);

              useEffect(() => {
                if (divRef && divRef.current && divRef.current.firstChild instanceof SVGSVGElement) {
                  const svgData: string = buildSvg(
                    imageWidth,
                    imageHeight,
                    `scale(${viewport.zoom})`,
                    divRef.current.firstChild,
                    style.cloneNode(true) as SVGStyleElement,
                    otherViewport
                  );
                  callback(URL.createObjectURL(new Blob([svgData], { type: 'image/svg+xml' })), textSvg);
                  hiddenContainer.parentNode?.removeChild(hiddenContainer);
                }
              }, [divRef]);

              return <div ref={divRef} dangerouslySetInnerHTML={{ __html: textSvg }} />;
            };

            const root = createRoot(hiddenContainer);
            root.render(<Element />);
          }
        });
    }
  }, []);

  const buildSvg = (
    width: number,
    height: number,
    transform: string,
    element: SVGSVGElement,
    style: SVGStyleElement,
    htmlToImageViewport: HTMLElement
  ): string => {
    const elementRect: DOMRect = element.getBoundingClientRect();

    const { svg, svgDocument } = createSvg();
    svg.style.transform = transform;
    svg.style.width = width.toString();
    svg.style.height = height.toString();
    svg.appendChild(style);

    // NOTE: Not yet found the way to copy the font
    // TODO: I think the font is copied, but it is the wrong one. We are using 'Roboto' but 'Lato' if retrieved O.o
    const reactFlowNodeContainer: HTMLElement | null = element.querySelector<HTMLElement>('.react-flow__nodes');
    Array.from(reactFlowNodeContainer?.children ?? []).forEach((child) => {
      defaultSVGElementVisitor.forEach((visitor) => {
        if (visitor.canHandle(child)) {
          visitor.handle(child, svg, svgDocument, elementRect);
        }
      });
    });
    debugger;
    const reactFlowEdgeContainer: HTMLElement | null = element.querySelector<HTMLElement>('.react-flow__edges');
    const edgeMarkers: SVGSVGElement | null | undefined =
      reactFlowEdgeContainer?.querySelector<SVGSVGElement>('#edge-markers');
    if (edgeMarkers?.firstChild) {
      const clonedDefs: Node = edgeMarkers?.firstChild.cloneNode(true);
      svg.appendChild(clonedDefs);
      let nextEdge: Element | null = edgeMarkers.nextElementSibling;
      while (nextEdge instanceof SVGSVGElement) {
        const gElement = nextEdge.firstChild?.cloneNode(true);
        if (gElement instanceof SVGGElement) {
          gElement.style.transform = htmlToImageViewport.style.transform;
          svg.appendChild(gElement);
        }
        nextEdge = nextEdge.nextElementSibling;
      }
    }

    return new XMLSerializer().serializeToString(svgDocument);
  };

  return { protoExportToSvg: exportSvg };
};
