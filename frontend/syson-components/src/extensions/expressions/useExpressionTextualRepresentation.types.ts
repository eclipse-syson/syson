/*******************************************************************************
 * Copyright (c) 2026 Obeo.
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

import { FeatureValueExpressionProperties } from './ExpressionProperties.types';

export interface UseExpressionTextualRepresentationValue {
  editorState: GQLExpressionEditorState | null;
  loading: boolean;
}

export interface GQLGetExpressionTextualRepresentationVariables {
  editingContextId: string;
  elementId: string;
}

export interface GQLGetExpressionTextualRepresentationData {
  viewer: GQLGetExpressionTextualRepresentationViewer;
}

export interface GQLGetExpressionTextualRepresentationViewer {
  editingContext: GQLGetExpressionTextualRepresentationEditingContext;
}

export interface GQLGetExpressionTextualRepresentationEditingContext {
  expressionTextualRepresentation: GQLExpressionEditorState | null;
}

export interface GQLExpressionEditorState {
  textualRepresentation: string;
  featureValueProperties: FeatureValueExpressionProperties | null;
}
