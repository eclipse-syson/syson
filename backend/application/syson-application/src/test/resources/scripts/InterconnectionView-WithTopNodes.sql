--
-- PostgreSQL database dump
--

-- Dumped from database version 12.11 (Debian 12.11-1.pgdg110+1)
-- Dumped by pg_dump version 17.0

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;

SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Data for Name: semantic_data; Type: TABLE DATA; Schema: public; Owner: dbuser
--

INSERT INTO public.semantic_data (id, created_on, last_modified_on) VALUES ('d7bf64ef-d408-437e-96b1-13b097c87f0e', '2024-10-21 16:14:50.119871+00', '2024-10-21 16:16:53.469378+00');


--
-- Data for Name: document; Type: TABLE DATA; Schema: public; Owner: dbuser
--

INSERT INTO public.document (id, semantic_data_id, name, content, created_on, last_modified_on) VALUES ('b945da33-845e-42c4-b2dd-a0aeca6e0996', 'd7bf64ef-d408-437e-96b1-13b097c87f0e', 'SysMLv2.sysml', '{
  "json": { "version": "1.0", "encoding": "utf-8" },
  "ns": { "sysml": "http://www.eclipse.org/syson/sysml" },
  "content": [
    {
      "id": "5d36000a-777b-4861-87b0-e50e7be12fe3",
      "eClass": "sysml:Namespace",
      "data": {
        "elementId": "c79a36b3-93a5-4761-8642-b2c32daeb57f",
        "ownedRelationship": [
          {
            "id": "a46a70de-c88e-4d31-919c-f2f014c3294e",
            "eClass": "sysml:OwningMembership",
            "data": {
              "elementId": "0285a973-c21d-42cd-b89b-1f602939672c",
              "ownedRelatedElement": [
                {
                  "id": "40d594df-bd94-45aa-9c85-39daa93c4a63",
                  "eClass": "sysml:Package",
                  "data": {
                    "declaredName": "Package 1",
                    "elementId": "6bb91732-42f7-468a-bb3e-a43f49851251",
                    "ownedRelationship": [
                      {
                        "id": "4a8e2dff-5c69-4c17-8cd1-e8a55dcd4ecc",
                        "eClass": "sysml:OwningMembership",
                        "data": {
                          "elementId": "2383ac77-a228-4ccf-9946-d374e14e8df6",
                          "ownedRelatedElement": [
                            {
                              "id": "5465efdc-f100-4854-9f86-d09f63de1d3b",
                              "eClass": "sysml:PartUsage",
                              "data": {
                                "declaredName": "partDiagram",
                                "elementId": "3f59dd09-1dce-4b74-a51c-450a986c4cbc",
                                "ownedRelationship": [
                                  {
                                    "id": "811452d5-1fb0-4bf7-ac02-fb14e40b70c8",
                                    "eClass": "sysml:FeatureMembership",
                                    "data": {
                                      "elementId": "6237b1e6-3283-48d0-aec6-65b1a6a61f46",
                                      "ownedRelatedElement": [
                                        {
                                          "id": "11f3e947-7fbe-48c2-b449-fa19be8edc69",
                                          "eClass": "sysml:PartUsage",
                                          "data": {
                                            "declaredName": "part1",
                                            "elementId": "f1b67c85-bb43-46f4-8ee5-a282d787c9ed",
                                            "isComposite": true
                                          }
                                        }
                                      ]
                                    }
                                  },
                                  {
                                    "id": "b35143d8-2082-4cf2-8706-aa6ccf84beed",
                                    "eClass": "sysml:FeatureMembership",
                                    "data": {
                                      "elementId": "046f2003-f0d3-4c2b-81aa-814f31abf5e6",
                                      "ownedRelatedElement": [
                                        {
                                          "id": "8228fefc-af27-4c68-b220-32fce02b09a8",
                                          "eClass": "sysml:ActionUsage",
                                          "data": {
                                            "declaredName": "action1",
                                            "elementId": "03b46bd3-dc17-402c-80f6-4088fd4afd59",
                                            "isComposite": true
                                          }
                                        }
                                      ]
                                    }
                                  },
                                  {
                                    "id": "910888bb-ba23-4ba6-8948-f475383a303a",
                                    "eClass": "sysml:FeatureMembership",
                                    "data": {
                                      "elementId": "915daa56-d73f-43bd-92d1-f14b953d8027",
                                      "ownedRelatedElement": [
                                        {
                                          "id": "94d75fa6-b64c-4e85-8600-9ef8cb68bf64",
                                          "eClass": "sysml:PortUsage",
                                          "data": {
                                            "declaredName": "port1",
                                            "elementId": "8afc0351-76d5-4e33-862a-3a9a575a1427",
                                            "isComposite": true
                                          }
                                        }
                                      ]
                                    }
                                  },
                                  {
                                    "id": "fdf1dd17-e69c-4218-8221-8a5263f8b8d5",
                                    "eClass": "sysml:FeatureMembership",
                                    "data": {
                                      "elementId": "10b069c3-e1e2-4503-9bc2-ac6ff5df071a",
                                      "ownedRelatedElement": [
                                        {
                                          "id": "8816769f-53c3-4497-b7c9-ec144f280374",
                                          "eClass": "sysml:PortUsage",
                                          "data": {
                                            "declaredName": "port2",
                                            "elementId": "5000273c-bbe7-47b8-b7e5-d1953e3da0da",
                                            "direction": "in",
                                            "isComposite": true
                                          }
                                        }
                                      ]
                                    }
                                  },
                                  {
                                    "id": "1c7dbfbc-e57d-4d9c-a431-af8a6877148e",
                                    "eClass": "sysml:FeatureMembership",
                                    "data": {
                                      "elementId": "7f8870c5-47dd-4db4-9d05-5231d6ced0fd",
                                      "ownedRelatedElement": [
                                        {
                                          "id": "ab91339a-cdc1-4e1c-a43e-15526722727b",
                                          "eClass": "sysml:PortUsage",
                                          "data": {
                                            "declaredName": "port3",
                                            "elementId": "4fc50740-c247-4e86-a5bb-d5a923c8a223",
                                            "direction": "inout",
                                            "isComposite": true
                                          }
                                        }
                                      ]
                                    }
                                  },
                                  {
                                    "id": "02837bd2-9db9-40b5-b274-a23bdfc8ba26",
                                    "eClass": "sysml:FeatureMembership",
                                    "data": {
                                      "elementId": "4b48e0f8-d7ca-42d5-bbbc-92104a4f7a6f",
                                      "ownedRelatedElement": [
                                        {
                                          "id": "59cad67f-0735-4baf-b98b-12564c620bf0",
                                          "eClass": "sysml:PortUsage",
                                          "data": {
                                            "declaredName": "port4",
                                            "elementId": "8417123c-c860-4210-b8b5-1af1ce63b7f5",
                                            "direction": "out",
                                            "isComposite": true
                                          }
                                        }
                                      ]
                                    }
                                  }
                                ],
                                "isComposite": true
                              }
                            }
                          ]
                        }
                      }
                    ]
                  }
                }
              ]
            }
          }
        ]
      }
    }
  ]
}', '2024-10-21 16:16:53.469378+00', '2024-10-21 16:16:53.469378+00');


--
-- Data for Name: image; Type: TABLE DATA; Schema: public; Owner: dbuser
--



--
-- Data for Name: library; Type: TABLE DATA; Schema: public; Owner: dbuser
--



--
-- Data for Name: project; Type: TABLE DATA; Schema: public; Owner: dbuser
--

INSERT INTO public.project (id, name, created_on, last_modified_on) VALUES ('411aab7b-892d-41d1-b9e2-ca10f901bb45', 'InterconnectionView-WithTopNodes', '2024-10-21 16:14:50.105313+00', '2024-10-21 16:17:27.434123+00');


--
-- Data for Name: nature; Type: TABLE DATA; Schema: public; Owner: dbuser
--



--
-- Data for Name: project_image; Type: TABLE DATA; Schema: public; Owner: dbuser
--



--
-- Data for Name: project_semantic_data; Type: TABLE DATA; Schema: public; Owner: dbuser
--

INSERT INTO public.project_semantic_data (id, project_id, semantic_data_id, name, created_on, last_modified_on) VALUES ('5fb8bc4a-1e76-483f-bdbb-4a0b7618c968', '411aab7b-892d-41d1-b9e2-ca10f901bb45', 'd7bf64ef-d408-437e-96b1-13b097c87f0e', 'main', '2024-10-21 16:14:50.119871+00', '2024-10-21 16:16:53.469378+00');


--
-- Data for Name: representation_metadata; Type: TABLE DATA; Schema: public; Owner: dbuser
--

INSERT INTO public.representation_metadata (id, target_object_id, description_id, label, kind, created_on, last_modified_on, documentation, semantic_data_id) VALUES ('ea522a25-80fd-4e16-8256-5a34e4cb687b', '5465efdc-f100-4854-9f86-d09f63de1d3b', 'siriusComponents://representationDescription?kind=diagramDescription&sourceKind=view&sourceId=74c5d045-51d7-359f-9634-611d0f1bef3d&sourceElementId=e1bd3b6d-357b-3068-b2e9-e0c1e19d6856', 'Interconnection View', 'siriusComponents://representation?type=Diagram', '2024-01-01 09:42:00+00', '2024-01-02 09:42:00+00', '', 'd7bf64ef-d408-437e-96b1-13b097c87f0e');


--
-- Data for Name: representation_content; Type: TABLE DATA; Schema: public; Owner: dbuser
--

INSERT INTO public.representation_content (id, content, last_migration_performed, migration_version, created_on, last_modified_on) VALUES ('ea522a25-80fd-4e16-8256-5a34e4cb687b', '{"id":"ea522a25-80fd-4e16-8256-5a34e4cb687b","kind":"siriusComponents://representation?type=Diagram","targetObjectId":"5465efdc-f100-4854-9f86-d09f63de1d3b","descriptionId":"siriusComponents://representationDescription?kind=diagramDescription&sourceKind=view&sourceId=74c5d045-51d7-359f-9634-611d0f1bef3d&sourceElementId=e1bd3b6d-357b-3068-b2e9-e0c1e19d6856","nodes":[{"id":"95d51162-e26f-36b8-8352-00580871fbf6","type":"node:rectangle","targetObjectId":"5465efdc-f100-4854-9f86-d09f63de1d3b","targetObjectKind":"siriusComponents://semantic?domain=sysml&entity=PartUsage","targetObjectLabel":"partDiagram","descriptionId":"siriusComponents://nodeDescription?sourceKind=view&sourceId=74c5d045-51d7-359f-9634-611d0f1bef3d&sourceElementId=4055a909-560d-3db5-a777-ea29c14cc2c2","borderNode":false,"modifiers":[],"state":"Normal","collapsingState":"EXPANDED","insideLabel":{"id":"5702d47e-35d5-37bc-a78a-55b3bdfe206b","text":"«part»\npartDiagram","insideLabelLocation":"TOP_CENTER","style":{"color":"#000000","fontSize":14,"bold":false,"italic":false,"underline":false,"strikeThrough":false,"iconURL":["/icons/full/obj16/PartUsage.svg"],"background":"transparent","borderColor":"black","borderSize":0,"borderRadius":3,"borderStyle":"Solid","maxWidth":null},"isHeader":true,"headerSeparatorDisplayMode":"NEVER","overflowStrategy":"NONE","textAlign":"CENTER"},"outsideLabels":[],"style":{"background":"#ffffff","borderColor":"#000000","borderSize":1,"borderRadius":10,"borderStyle":"Solid"},"childrenLayoutStrategy":{"kind":"FreeForm"},"borderNodes":[{"id":"91fde15e-ce5e-33ae-941e-6c7e1e30f739","type":"node:rectangle","targetObjectId":"94d75fa6-b64c-4e85-8600-9ef8cb68bf64","targetObjectKind":"siriusComponents://semantic?domain=sysml&entity=PortUsage","targetObjectLabel":"port1","descriptionId":"siriusComponents://nodeDescription?sourceKind=view&sourceId=74c5d045-51d7-359f-9634-611d0f1bef3d&sourceElementId=9e813b81-da01-3300-8d02-c881f85ccadc","borderNode":true,"modifiers":[],"state":"Normal","collapsingState":"EXPANDED","insideLabel":null,"outsideLabels":[{"id":"196b3f85-5c18-3c6f-a209-1b9b848b012e","text":"port1","outsideLabelLocation":"BOTTOM_MIDDLE","style":{"color":"#000000","fontSize":12,"bold":false,"italic":false,"underline":false,"strikeThrough":false,"iconURL":[],"background":"transparent","borderColor":"black","borderSize":0,"borderRadius":3,"borderStyle":"Solid","maxWidth":null},"overflowStrategy":"NONE","textAlign":"LEFT"}],"style":{"background":"#ffffff","borderColor":"#000000","borderSize":1,"borderRadius":0,"borderStyle":"Solid"},"childrenLayoutStrategy":null,"borderNodes":[],"childNodes":[],"defaultWidth":10,"defaultHeight":10,"labelEditable":true,"pinned":false},{"id":"836f0e1b-083d-3bc0-8e51-5d6d3981a7e7","type":"node:image","targetObjectId":"8816769f-53c3-4497-b7c9-ec144f280374","targetObjectKind":"siriusComponents://semantic?domain=sysml&entity=PortUsage","targetObjectLabel":"port2","descriptionId":"siriusComponents://nodeDescription?sourceKind=view&sourceId=74c5d045-51d7-359f-9634-611d0f1bef3d&sourceElementId=9e813b81-da01-3300-8d02-c881f85ccadc","borderNode":true,"modifiers":[],"state":"Normal","collapsingState":"EXPANDED","insideLabel":null,"outsideLabels":[{"id":"c42aaacc-8d88-37ab-84b5-0604dbfcf307","text":"port2","outsideLabelLocation":"BOTTOM_MIDDLE","style":{"color":"#000000","fontSize":12,"bold":false,"italic":false,"underline":false,"strikeThrough":false,"iconURL":[],"background":"transparent","borderColor":"black","borderSize":0,"borderRadius":3,"borderStyle":"Solid","maxWidth":null},"overflowStrategy":"NONE","textAlign":"LEFT"}],"style":{"imageURL":"images/feature_in.svg","scalingFactor":1,"borderColor":"#000000","borderSize":1,"borderRadius":0,"borderStyle":"Solid","positionDependentRotation":true},"childrenLayoutStrategy":null,"borderNodes":[],"childNodes":[],"defaultWidth":10,"defaultHeight":10,"labelEditable":true,"pinned":false},{"id":"dd062e44-c9d1-384f-84c1-19049a8fd841","type":"node:image","targetObjectId":"ab91339a-cdc1-4e1c-a43e-15526722727b","targetObjectKind":"siriusComponents://semantic?domain=sysml&entity=PortUsage","targetObjectLabel":"port3","descriptionId":"siriusComponents://nodeDescription?sourceKind=view&sourceId=74c5d045-51d7-359f-9634-611d0f1bef3d&sourceElementId=9e813b81-da01-3300-8d02-c881f85ccadc","borderNode":true,"modifiers":[],"state":"Normal","collapsingState":"EXPANDED","insideLabel":null,"outsideLabels":[{"id":"89ba6db0-1775-32ba-8e05-08f9a17bff03","text":"port3","outsideLabelLocation":"BOTTOM_MIDDLE","style":{"color":"#000000","fontSize":12,"bold":false,"italic":false,"underline":false,"strikeThrough":false,"iconURL":[],"background":"transparent","borderColor":"black","borderSize":0,"borderRadius":3,"borderStyle":"Solid","maxWidth":null},"overflowStrategy":"NONE","textAlign":"LEFT"}],"style":{"imageURL":"images/feature_inout.svg","scalingFactor":1,"borderColor":"#000000","borderSize":1,"borderRadius":0,"borderStyle":"Solid","positionDependentRotation":true},"childrenLayoutStrategy":null,"borderNodes":[],"childNodes":[],"defaultWidth":10,"defaultHeight":10,"labelEditable":true,"pinned":false},{"id":"ba2f89c3-7910-30c9-9acc-1ff04f6c5756","type":"node:image","targetObjectId":"59cad67f-0735-4baf-b98b-12564c620bf0","targetObjectKind":"siriusComponents://semantic?domain=sysml&entity=PortUsage","targetObjectLabel":"port4","descriptionId":"siriusComponents://nodeDescription?sourceKind=view&sourceId=74c5d045-51d7-359f-9634-611d0f1bef3d&sourceElementId=9e813b81-da01-3300-8d02-c881f85ccadc","borderNode":true,"modifiers":[],"state":"Normal","collapsingState":"EXPANDED","insideLabel":null,"outsideLabels":[{"id":"9d0d9ad0-8006-3a32-b0e6-71abc436838c","text":"port4","outsideLabelLocation":"BOTTOM_MIDDLE","style":{"color":"#000000","fontSize":12,"bold":false,"italic":false,"underline":false,"strikeThrough":false,"iconURL":[],"background":"transparent","borderColor":"black","borderSize":0,"borderRadius":3,"borderStyle":"Solid","maxWidth":null},"overflowStrategy":"NONE","textAlign":"LEFT"}],"style":{"imageURL":"images/feature_out.svg","scalingFactor":1,"borderColor":"#000000","borderSize":1,"borderRadius":0,"borderStyle":"Solid","positionDependentRotation":true},"childrenLayoutStrategy":null,"borderNodes":[],"childNodes":[],"defaultWidth":10,"defaultHeight":10,"labelEditable":true,"pinned":false}],"childNodes":[{"id":"36f78d91-97a2-366d-98e4-c2f9fb5d4f88","type":"node:rectangle","targetObjectId":"11f3e947-7fbe-48c2-b449-fa19be8edc69","targetObjectKind":"siriusComponents://semantic?domain=sysml&entity=PartUsage","targetObjectLabel":"part1","descriptionId":"siriusComponents://nodeDescription?sourceKind=view&sourceId=74c5d045-51d7-359f-9634-611d0f1bef3d&sourceElementId=700133eb-7806-3dce-98d2-9ec37d212aaa","borderNode":false,"modifiers":[],"state":"Normal","collapsingState":"EXPANDED","insideLabel":{"id":"72273efa-4987-355d-a382-b960b4575a42","text":"«part»\npart1","insideLabelLocation":"TOP_CENTER","style":{"color":"#000000","fontSize":14,"bold":false,"italic":false,"underline":false,"strikeThrough":false,"iconURL":["/icons/full/obj16/PartUsage.svg"],"background":"transparent","borderColor":"black","borderSize":0,"borderRadius":3,"borderStyle":"Solid","maxWidth":null},"isHeader":true,"headerSeparatorDisplayMode":"IF_CHILDREN","overflowStrategy":"NONE","textAlign":"CENTER"},"outsideLabels":[],"style":{"background":"#ffffff","borderColor":"#000000","borderSize":1,"borderRadius":10,"borderStyle":"Solid"},"childrenLayoutStrategy":{"areChildNodesDraggable":false,"topGap":0,"bottomGap":0,"growableNodeIds":["siriusComponents://nodeDescription?sourceKind=view&sourceId=74c5d045-51d7-359f-9634-611d0f1bef3d&sourceElementId=a77b4c6c-526d-3fe4-822b-de505f743123"],"kind":"List"},"borderNodes":[],"childNodes":[{"id":"ccd9eae5-616f-36ca-bb61-24cb7613bf62","type":"node:rectangle","targetObjectId":"11f3e947-7fbe-48c2-b449-fa19be8edc69","targetObjectKind":"siriusComponents://semantic?domain=sysml&entity=PartUsage","targetObjectLabel":"part1","descriptionId":"siriusComponents://nodeDescription?sourceKind=view&sourceId=74c5d045-51d7-359f-9634-611d0f1bef3d&sourceElementId=9f871f18-dbda-3798-8e28-094acfae6a2d","borderNode":false,"modifiers":["Hidden"],"state":"Hidden","collapsingState":"EXPANDED","insideLabel":{"id":"1c8d4418-3557-30c2-8e93-2da74eea469d","text":"doc","insideLabelLocation":"TOP_CENTER","style":{"color":"#000000","fontSize":12,"bold":false,"italic":true,"underline":false,"strikeThrough":false,"iconURL":[],"background":"transparent","borderColor":"black","borderSize":0,"borderRadius":3,"borderStyle":"Solid","maxWidth":null},"isHeader":true,"headerSeparatorDisplayMode":"NEVER","overflowStrategy":"NONE","textAlign":"CENTER"},"outsideLabels":[],"style":{"background":"transparent","borderColor":"#000000","borderSize":1,"borderRadius":0,"borderStyle":"Solid"},"childrenLayoutStrategy":{"areChildNodesDraggable":true,"topGap":0,"bottomGap":10,"growableNodeIds":[],"kind":"List"},"borderNodes":[],"childNodes":[],"defaultWidth":155,"defaultHeight":60,"labelEditable":false,"pinned":false},{"id":"70c8561c-accf-3b80-b303-1144e1554682","type":"node:rectangle","targetObjectId":"11f3e947-7fbe-48c2-b449-fa19be8edc69","targetObjectKind":"siriusComponents://semantic?domain=sysml&entity=PartUsage","targetObjectLabel":"part1","descriptionId":"siriusComponents://nodeDescription?sourceKind=view&sourceId=74c5d045-51d7-359f-9634-611d0f1bef3d&sourceElementId=85d2283f-c727-34f0-ba1b-695e544d15a7","borderNode":false,"modifiers":["Hidden"],"state":"Hidden","collapsingState":"EXPANDED","insideLabel":{"id":"a7048cdc-7729-3bd4-ad82-959f5bcbbfcb","text":"attributes","insideLabelLocation":"TOP_CENTER","style":{"color":"#000000","fontSize":12,"bold":false,"italic":true,"underline":false,"strikeThrough":false,"iconURL":[],"background":"transparent","borderColor":"black","borderSize":0,"borderRadius":3,"borderStyle":"Solid","maxWidth":null},"isHeader":true,"headerSeparatorDisplayMode":"NEVER","overflowStrategy":"NONE","textAlign":"CENTER"},"outsideLabels":[],"style":{"background":"transparent","borderColor":"#000000","borderSize":1,"borderRadius":0,"borderStyle":"Solid"},"childrenLayoutStrategy":{"areChildNodesDraggable":true,"topGap":0,"bottomGap":10,"growableNodeIds":[],"kind":"List"},"borderNodes":[],"childNodes":[],"defaultWidth":155,"defaultHeight":60,"labelEditable":false,"pinned":false},{"id":"c9be0d7e-ab94-3295-9941-c14ac2ed88c2","type":"node:rectangle","targetObjectId":"11f3e947-7fbe-48c2-b449-fa19be8edc69","targetObjectKind":"siriusComponents://semantic?domain=sysml&entity=PartUsage","targetObjectLabel":"part1","descriptionId":"siriusComponents://nodeDescription?sourceKind=view&sourceId=74c5d045-51d7-359f-9634-611d0f1bef3d&sourceElementId=a77b4c6c-526d-3fe4-822b-de505f743123","borderNode":false,"modifiers":["Hidden"],"state":"Hidden","collapsingState":"EXPANDED","insideLabel":{"id":"e493504b-d963-3017-8cce-6eb71f676a1c","text":"parts","insideLabelLocation":"TOP_CENTER","style":{"color":"#000000","fontSize":12,"bold":false,"italic":true,"underline":false,"strikeThrough":false,"iconURL":[],"background":"transparent","borderColor":"black","borderSize":0,"borderRadius":3,"borderStyle":"Solid","maxWidth":null},"isHeader":true,"headerSeparatorDisplayMode":"NEVER","overflowStrategy":"NONE","textAlign":"CENTER"},"outsideLabels":[],"style":{"background":"transparent","borderColor":"#000000","borderSize":1,"borderRadius":0,"borderStyle":"Solid"},"childrenLayoutStrategy":{"kind":"FreeForm"},"borderNodes":[],"childNodes":[],"defaultWidth":155,"defaultHeight":150,"labelEditable":false,"pinned":false}],"defaultWidth":150,"defaultHeight":60,"labelEditable":true,"pinned":false},{"id":"68e15d10-aa29-36bf-bdd2-d1c70fa9f1d5","type":"node:rectangle","targetObjectId":"8228fefc-af27-4c68-b220-32fce02b09a8","targetObjectKind":"siriusComponents://semantic?domain=sysml&entity=ActionUsage","targetObjectLabel":"action1","descriptionId":"siriusComponents://nodeDescription?sourceKind=view&sourceId=74c5d045-51d7-359f-9634-611d0f1bef3d&sourceElementId=f19c2b1a-9494-3c07-b567-6a58b215e647","borderNode":false,"modifiers":[],"state":"Normal","collapsingState":"EXPANDED","insideLabel":{"id":"d78fc830-a549-34c6-adaf-e631edbcbda3","text":"«action»\naction1","insideLabelLocation":"TOP_CENTER","style":{"color":"#000000","fontSize":14,"bold":false,"italic":false,"underline":false,"strikeThrough":false,"iconURL":["/icons/full/obj16/ActionUsage.svg"],"background":"transparent","borderColor":"black","borderSize":0,"borderRadius":3,"borderStyle":"Solid","maxWidth":null},"isHeader":true,"headerSeparatorDisplayMode":"IF_CHILDREN","overflowStrategy":"NONE","textAlign":"CENTER"},"outsideLabels":[],"style":{"background":"#ffffff","borderColor":"#000000","borderSize":1,"borderRadius":10,"borderStyle":"Solid"},"childrenLayoutStrategy":{"areChildNodesDraggable":false,"topGap":0,"bottomGap":0,"growableNodeIds":["siriusComponents://nodeDescription?sourceKind=view&sourceId=74c5d045-51d7-359f-9634-611d0f1bef3d&sourceElementId=09bac8f5-e6f0-3fb0-a612-a7242d9a256c"],"kind":"List"},"borderNodes":[],"childNodes":[{"id":"42295dc9-e3f9-3991-9d6d-fa04008198de","type":"node:rectangle","targetObjectId":"8228fefc-af27-4c68-b220-32fce02b09a8","targetObjectKind":"siriusComponents://semantic?domain=sysml&entity=ActionUsage","targetObjectLabel":"action1","descriptionId":"siriusComponents://nodeDescription?sourceKind=view&sourceId=74c5d045-51d7-359f-9634-611d0f1bef3d&sourceElementId=0cbcdebe-c87d-3e4d-ab50-fe886f65cee6","borderNode":false,"modifiers":["Hidden"],"state":"Hidden","collapsingState":"EXPANDED","insideLabel":{"id":"f0286d15-9407-3cc1-b7b9-5612458edd05","text":"doc","insideLabelLocation":"TOP_CENTER","style":{"color":"#000000","fontSize":12,"bold":false,"italic":true,"underline":false,"strikeThrough":false,"iconURL":[],"background":"transparent","borderColor":"black","borderSize":0,"borderRadius":3,"borderStyle":"Solid","maxWidth":null},"isHeader":true,"headerSeparatorDisplayMode":"NEVER","overflowStrategy":"NONE","textAlign":"CENTER"},"outsideLabels":[],"style":{"background":"transparent","borderColor":"#000000","borderSize":1,"borderRadius":0,"borderStyle":"Solid"},"childrenLayoutStrategy":{"areChildNodesDraggable":true,"topGap":0,"bottomGap":10,"growableNodeIds":[],"kind":"List"},"borderNodes":[],"childNodes":[],"defaultWidth":155,"defaultHeight":60,"labelEditable":false,"pinned":false},{"id":"29147ab0-645b-3b1d-acd6-71d8d9d33402","type":"node:rectangle","targetObjectId":"8228fefc-af27-4c68-b220-32fce02b09a8","targetObjectKind":"siriusComponents://semantic?domain=sysml&entity=ActionUsage","targetObjectLabel":"action1","descriptionId":"siriusComponents://nodeDescription?sourceKind=view&sourceId=74c5d045-51d7-359f-9634-611d0f1bef3d&sourceElementId=09bac8f5-e6f0-3fb0-a612-a7242d9a256c","borderNode":false,"modifiers":["Hidden"],"state":"Hidden","collapsingState":"EXPANDED","insideLabel":{"id":"b449f1b7-378e-35a7-8de7-80a8a85290d3","text":"action flow","insideLabelLocation":"TOP_CENTER","style":{"color":"#000000","fontSize":12,"bold":false,"italic":true,"underline":false,"strikeThrough":false,"iconURL":[],"background":"transparent","borderColor":"black","borderSize":0,"borderRadius":3,"borderStyle":"Solid","maxWidth":null},"isHeader":true,"headerSeparatorDisplayMode":"NEVER","overflowStrategy":"NONE","textAlign":"CENTER"},"outsideLabels":[],"style":{"background":"transparent","borderColor":"#000000","borderSize":1,"borderRadius":0,"borderStyle":"Solid"},"childrenLayoutStrategy":{"kind":"FreeForm"},"borderNodes":[],"childNodes":[],"defaultWidth":155,"defaultHeight":150,"labelEditable":false,"pinned":false}],"defaultWidth":150,"defaultHeight":60,"labelEditable":true,"pinned":false}],"defaultWidth":700,"defaultHeight":400,"labelEditable":true,"pinned":false}],"edges":[],"layoutData":{"nodeLayoutData":{"95d51162-e26f-36b8-8352-00580871fbf6":{"id":"95d51162-e26f-36b8-8352-00580871fbf6","position":{"x":46.93090048555359,"y":-85.93267355103484},"size":{"width":700.0,"height":400.0},"resizedByUser":false},"ba2f89c3-7910-30c9-9acc-1ff04f6c5756":{"id":"ba2f89c3-7910-30c9-9acc-1ff04f6c5756","position":{"x":695.0,"y":116.0},"size":{"width":16.0,"height":16.0},"resizedByUser":false},"dd062e44-c9d1-384f-84c1-19049a8fd841":{"id":"dd062e44-c9d1-384f-84c1-19049a8fd841","position":{"x":695.0,"y":80.0},"size":{"width":16.0,"height":16.0},"resizedByUser":false},"29147ab0-645b-3b1d-acd6-71d8d9d33402":{"id":"29147ab0-645b-3b1d-acd6-71d8d9d33402","position":{"x":0.0,"y":0.0},"size":{"width":125.0,"height":150.0},"resizedByUser":false},"42295dc9-e3f9-3991-9d6d-fa04008198de":{"id":"42295dc9-e3f9-3991-9d6d-fa04008198de","position":{"x":0.0,"y":0.0},"size":{"width":125.0,"height":60.0},"resizedByUser":false},"36f78d91-97a2-366d-98e4-c2f9fb5d4f88":{"id":"36f78d91-97a2-366d-98e4-c2f9fb5d4f88","position":{"x":93.33333333333329,"y":127.7237739675516},"size":{"width":150.0,"height":60.0},"resizedByUser":false},"ccd9eae5-616f-36ca-bb61-24cb7613bf62":{"id":"ccd9eae5-616f-36ca-bb61-24cb7613bf62","position":{"x":0.0,"y":0.0},"size":{"width":125.0,"height":60.0},"resizedByUser":false},"68e15d10-aa29-36bf-bdd2-d1c70fa9f1d5":{"id":"68e15d10-aa29-36bf-bdd2-d1c70fa9f1d5","position":{"x":331.9354978409529,"y":127.72377396755161},"size":{"width":150.0,"height":60.0},"resizedByUser":false},"c9be0d7e-ab94-3295-9941-c14ac2ed88c2":{"id":"c9be0d7e-ab94-3295-9941-c14ac2ed88c2","position":{"x":0.0,"y":0.0},"size":{"width":125.0,"height":150.0},"resizedByUser":false},"70c8561c-accf-3b80-b303-1144e1554682":{"id":"70c8561c-accf-3b80-b303-1144e1554682","position":{"x":0.0,"y":0.0},"size":{"width":125.0,"height":60.0},"resizedByUser":false},"836f0e1b-083d-3bc0-8e51-5d6d3981a7e7":{"id":"836f0e1b-083d-3bc0-8e51-5d6d3981a7e7","position":{"x":695.0,"y":44.0},"size":{"width":16.0,"height":16.0},"resizedByUser":false},"91fde15e-ce5e-33ae-941e-6c7e1e30f739":{"id":"91fde15e-ce5e-33ae-941e-6c7e1e30f739","position":{"x":695.0,"y":8.0},"size":{"width":16.0,"height":16.0},"resizedByUser":false}},"edgeLayoutData":{},"labelLayoutData":{}}}', 'none', '2024.5.4-202407040900', '2024-01-01 09:42:00+00', '2025-04-01 13:08:25.964852+00');


--
-- Data for Name: semantic_data_dependency; Type: TABLE DATA; Schema: public; Owner: dbuser
--



--
-- Data for Name: semantic_data_domain; Type: TABLE DATA; Schema: public; Owner: dbuser
--

INSERT INTO public.semantic_data_domain (semantic_data_id, uri) VALUES ('d7bf64ef-d408-437e-96b1-13b097c87f0e', 'http://www.eclipse.org/syson/sysml');


--
-- PostgreSQL database dump complete
--

