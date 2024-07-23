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

import { DocumentTransform } from '@apollo/client';
import { DocumentNode, FieldNode, InlineFragmentNode, Kind, SelectionNode, visit } from 'graphql';

const shouldTransform = (document: DocumentNode) => {
  return (
    document.definitions[0] &&
    document.definitions[0].kind === Kind.OPERATION_DEFINITION &&
    (document.definitions[0].name?.value === 'detailsEvent' || document.definitions[0].name?.value === 'formEvent')
  );
};

const isWidgetFragment = (field: FieldNode) => {
  if (field.name.value === 'widgets') {
    const inLinesFragment = field.selectionSet.selections
      .filter((selection): selection is InlineFragmentNode => selection.kind === Kind.INLINE_FRAGMENT)
      .map((inlineFragment: InlineFragmentNode) => inlineFragment.typeCondition.name.value);
    if (inLinesFragment.includes('FlexboxContainer')) {
      return true;
    }
  }
  return false;
};

const labelField: SelectionNode = {
  kind: Kind.FIELD,
  name: {
    kind: Kind.NAME,
    value: 'label',
  },
};

const iconURLField: SelectionNode = {
  kind: Kind.FIELD,
  name: {
    kind: Kind.NAME,
    value: 'iconURL',
  },
};

const ownerIdField: SelectionNode = {
  kind: Kind.FIELD,
  name: {
    kind: Kind.NAME,
    value: 'ownerId',
  },
};

const descriptionIdField: SelectionNode = {
  kind: Kind.FIELD,
  name: {
    kind: Kind.NAME,
    value: 'descriptionId',
  },
};

const ownerKindField: SelectionNode = {
  kind: Kind.FIELD,
  name: {
    kind: Kind.NAME,
    value: 'ownerKind',
  },
};

const referenceKindField: SelectionNode = {
  kind: Kind.FIELD,
  name: {
    kind: Kind.NAME,
    value: 'referenceKind',
  },
};

const containmentField: SelectionNode = {
  kind: Kind.FIELD,
  name: {
    kind: Kind.NAME,
    value: 'containment',
  },
};

const manyValuedField: SelectionNode = {
  kind: Kind.FIELD,
  name: {
    kind: Kind.NAME,
    value: 'manyValued',
  },
};

const referenceField: SelectionNode = {
  kind: Kind.FIELD,
  name: {
    kind: Kind.NAME,
    value: 'reference',
  },
  selectionSet: {
    kind: Kind.SELECTION_SET,
    selections: [ownerKindField, referenceKindField, containmentField, manyValuedField],
  },
};

const idField: SelectionNode = {
  kind: Kind.FIELD,
  name: {
    kind: Kind.NAME,
    value: 'id',
  },
};

const kindField: SelectionNode = {
  kind: Kind.FIELD,
  name: {
    kind: Kind.NAME,
    value: 'kind',
  },
};

const colorField: SelectionNode = {
  kind: Kind.FIELD,
  name: {
    kind: Kind.NAME,
    value: 'color',
  },
};

const fontSizeField: SelectionNode = {
  kind: Kind.FIELD,
  name: {
    kind: Kind.NAME,
    value: 'fontSize',
  },
};

const italicField: SelectionNode = {
  kind: Kind.FIELD,
  name: {
    kind: Kind.NAME,
    value: 'italic',
  },
};

const boldField: SelectionNode = {
  kind: Kind.FIELD,
  name: {
    kind: Kind.NAME,
    value: 'bold',
  },
};

const underlineField: SelectionNode = {
  kind: Kind.FIELD,
  name: {
    kind: Kind.NAME,
    value: 'underline',
  },
};

const strikeThroughField: SelectionNode = {
  kind: Kind.FIELD,
  name: {
    kind: Kind.NAME,
    value: 'strikeThrough',
  },
};

const referenceValuesField: SelectionNode = {
  kind: Kind.FIELD,
  name: {
    kind: Kind.NAME,
    value: 'referenceValues',
  },
  selectionSet: {
    kind: Kind.SELECTION_SET,
    selections: [idField, labelField, kindField, iconURLField],
  },
};

const styleField: SelectionNode = {
  kind: Kind.FIELD,
  name: {
    kind: Kind.NAME,
    value: 'style',
  },
  selectionSet: {
    kind: Kind.SELECTION_SET,
    selections: [colorField, fontSizeField, italicField, boldField, underlineField, strikeThroughField],
  },
};

export const referenceWidgetDocumentTransform = new DocumentTransform((document) => {
  if (shouldTransform(document)) {
    return visit(document, {
      Field(field) {
        if (!isWidgetFragment(field)) {
          return undefined;
        }
        const selections = field.selectionSet?.selections ?? [];

        const referenceWidgetInlineFragment: InlineFragmentNode = {
          kind: Kind.INLINE_FRAGMENT,
          selectionSet: {
            kind: Kind.SELECTION_SET,
            selections: [
              labelField,
              iconURLField,
              ownerIdField,
              descriptionIdField,
              referenceField,
              referenceValuesField,
              styleField,
            ],
          },
          typeCondition: {
            kind: Kind.NAMED_TYPE,
            name: {
              kind: Kind.NAME,
              value: 'ReferenceWidget',
            },
          },
        };

        return {
          ...field,
          selectionSet: {
            ...field.selectionSet,
            selections: [...selections, referenceWidgetInlineFragment],
          },
        };
      },
    });
  }
  return document;
});
