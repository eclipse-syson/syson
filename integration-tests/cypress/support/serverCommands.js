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

import { v4 as uuid } from 'uuid';
const url = Cypress.env('baseAPIUrl') + '/api/graphql';

Cypress.Commands.add('deleteAllProjects', () => {
  const getProjectsQuery = `
  query getProjects($page: Int!, $limit: Int!) {
    viewer {
      projects(page: $page, limit: $limit) {
        edges {
          node {
            id
          }
        }
      }
    }
  }
  `;
  cy.request({
    method: 'POST',
    mode: 'cors',
    url,
    body: { query: getProjectsQuery, variables: { page: 0, limit: 50 } },
  }).then((res) => {
    const projectIds = res.body.data.viewer.projects.edges.map((edge) => edge.node.id);

    const deleteProjectQuery = `
    mutation deleteProject($input: DeleteProjectInput!) {
      deleteProject(input: $input) {
        __typename
      }
    }
    `;
    projectIds.forEach((projectId) => {
      const variables = {
        input: {
          id: uuid(),
          projectId,
        },
      };
      cy.request({
        method: 'POST',
        mode: 'cors',
        url,
        body: { query: deleteProjectQuery, variables },
      });
    });
  });
});

Cypress.Commands.add('deleteNodeFromDiagram', (editingContextId, representationId, nodeId) => {
  const query = `
   mutation deleteFromDiagram($input: DeleteFromDiagramInput!) {
    deleteFromDiagram(input: $input) {
       __typename
       ... on DeleteFromDiagramSuccessPayload {
        diagram {
          id
        }
      }
      ... on ErrorPayload {
        message
      }
    }
   }
   `;
  const variables = {
    input: {
      id: uuid(),
      editingContextId,
      representationId,
      nodeIds: [nodeId],
      edgeIds: [],
      deletionPolicy: 'SEMANTIC',
    },
  };

  const body = {
    query,
    variables,
  };
  return cy.request({
    method: 'POST',
    mode: 'cors',
    url,
    body,
  });
});

/**
 * This command chains two requests. This first one needs the editing context id and the class id to get all
 * possible representation description. Then ...
 */
Cypress.Commands.add(
  'createRepresentation',
  (editingContextId, kind, representationLabel, objectId, representationName) => {
    const repDescriptionQuery = `
    query getRepresentationDescriptions($editingContextId: ID!, $kind: ID!) {
      viewer {
        editingContext(editingContextId: $editingContextId) {
          representationDescriptions(kind: $kind) {
            edges {
              node {
                id
                label
              }
            }
            pageInfo {
              hasNextPage
              hasPreviousPage
              startCursor
              endCursor
            }
          }
        }
      }
    }
    `;
    const repDescriptionQueryVariables = {
      editingContextId,
      kind,
    };

    const createRepMutation = `
    mutation createRepresentation($input: CreateRepresentationInput!) {
      createRepresentation(input: $input) {
        __typename
        ... on CreateRepresentationSuccessPayload {
          representation {
            __typename
            id
            label
          }
        }
      }
    }
    `;

    // Rework here when pages will be included in the input.
    // The rework will have to make a new request for the next
    // page until it finds the right element or return an error
    // if no element is found.
    return cy
      .request({
        method: 'POST',
        mode: 'cors',
        url,
        body: {
          query: repDescriptionQuery,
          variables: repDescriptionQueryVariables,
        },
      })
      .then((resp) => {
        expect(resp.status).to.eq(200);
        return resp.body.data.viewer.editingContext.representationDescriptions.edges
          .map((edge) => {
            return { ...edge.node };
          })
          .filter((node) => node.label === representationLabel)[0];
      })
      .then((representationDescriptions) => {
        const createRepMutationVariables = {
          input: {
            id: uuid(),
            editingContextId,
            objectId,
            representationName,
            representationDescriptionId: representationDescriptions.id,
          },
        };

        return cy
          .request({
            method: 'POST',
            mode: 'cors',
            url,
            body: {
              query: createRepMutation,
              variables: createRepMutationVariables,
            },
          })
          .then((resp) => {
            expect(resp.status).to.eq(200);
            const repId = resp.body.data.createRepresentation.representation.id;
            return repId;
          });
      });
  }
);
