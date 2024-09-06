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
/*
 * This code has been fully inspired from NoteNode.types.ts in https://github.com/PapyrusSirius/papyrus-web
 */ import { GQLNodeStyle, NodeData } from '@eclipse-sirius/sirius-components-diagrams';

export interface SysMLNoteNodeData extends NodeData {}

export interface GQLSysMLNoteNodeStyle extends GQLNodeStyle {
  background: string;
  borderColor: string;
  borderStyle: string;
  borderSize: string;
}
