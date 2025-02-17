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

import { SvgExportContext, svgNamespace } from './useProtoSvgExport.types';

export const createSvg = (): SvgExportContext => {
  const svgDocument = document.implementation.createDocument(svgNamespace, 'svg', null);
  const svg: SVGSVGElement = svgDocument.documentElement as unknown as SVGSVGElement;
  // svg.setAttribute('xmlns', 'http://www.w3.org/1999/xlink');
  // TODO: svglink namespace ?
  svg.setAttribute('xmlns:xlink', 'http://www.w3.org/1999/xlink');
  return {
    svg,
    svgDocument,
  };
};

// export const createRect = (svgDocument: XMLDocument): SVGRectElement => {};
