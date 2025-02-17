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

export const svgNamespace = 'http://www.w3.org/2000/svg';

export interface UseProtoSvgExport {
  protoExportToSvg: (callback: (result: string, expected: string) => void) => void;
}

export interface SvgExportContext {
  svg: SVGSVGElement;
  svgDocument: XMLDocument;
}
