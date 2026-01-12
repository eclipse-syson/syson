--
-- PostgreSQL database dump
--

-- Dumped from database version 12.19 (Debian 12.19-1.pgdg120+1)
-- Dumped by pg_dump version 17.4

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

INSERT INTO public.semantic_data (id, created_on, last_modified_on) VALUES ('38b7db6d-736d-45c3-ad08-d461774c83d9', '2024-06-07 09:37:50.664621+00', '2025-05-06 15:02:20.178286+00');


--
-- Data for Name: document; Type: TABLE DATA; Schema: public; Owner: dbuser
--

INSERT INTO public.document (id, semantic_data_id, name, content, is_read_only, created_on, last_modified_on) VALUES ('82b6c2a2-43ab-4a8d-baa6-920652fd963f', '38b7db6d-736d-45c3-ad08-d461774c83d9', 'SysMLv2', '{"json":{"version":"1.0","encoding":"utf-8"},"ns":{"sysml":"http://www.eclipse.org/syson/sysml"},"migration":{"lastMigrationPerformed":"EdgeDescriptionSourceTargetDescriptionsParticipant","migrationVersion":"2025.4.0-202503171117"},"content":[{"id":"3c8831a2-9759-40da-a6a2-4ef4f5939136","eClass":"sysml:Namespace","data":{"elementId":"e8745a10-90d3-43ce-9b17-f04abbf7a82c","ownedRelationship":[{"id":"4ed5fcdb-e177-44ed-b056-cb0fdf232089","eClass":"sysml:OwningMembership","data":{"elementId":"413cfbcd-6e75-4a12-806d-e453cb963471","ownedRelatedElement":[{"id":"528fae42-60c2-40c8-baa3-684d874a950d","eClass":"sysml:Package","data":{"declaredName":"Package 1","elementId":"b7050480-08ca-4536-8d52-3c7b74e88f19","ownedRelationship":[{"id":"72a9d135-4f63-4787-bfcb-d3afd5a51b88","eClass":"sysml:OwningMembership","data":{"elementId":"02bf0698-a0bb-4b84-b7c8-1461b3b01289","ownedRelatedElement":[{"id":"73f66477-a239-4dfe-88c9-bc99ee7a25ee","eClass":"sysml:ViewUsage","data":{"declaredName":"General View","elementId":"91943245-973a-451a-9c29-a139d3d288c0","ownedRelationship":[{"id":"ef95ea9a-403c-4844-baa0-48c4fa8c3cd1","eClass":"sysml:FeatureTyping","data":{"elementId":"2ec7df33-265a-4eeb-b2a1-6f05cc0d2fa0","type":"sysml:ViewDefinition sysmllibrary:///faf517ae-dbcd-30a4-b3b9-3d9cb3bbf5c1#03904fdf-d6f2-57b1-92d5-95d36b8208dc","typedFeature":"91943245-973a-451a-9c29-a139d3d288c0"}}]}}]}}]}}]}}]}}]}', false, '2025-05-06 15:02:20.178098+00', '2025-05-06 15:02:20.178098+00');


--
-- Data for Name: image; Type: TABLE DATA; Schema: public; Owner: dbuser
--



--
-- Data for Name: library; Type: TABLE DATA; Schema: public; Owner: dbuser
--



--
-- Data for Name: project; Type: TABLE DATA; Schema: public; Owner: dbuser
--

INSERT INTO public.project (id, name, created_on, last_modified_on) VALUES ('861bc7ba-defb-400c-982f-d6563f36af48', 'GeneralView-Empty', '2024-06-07 09:37:50.610195+00', '2024-06-13 09:44:17.544014+00');


--
-- Data for Name: nature; Type: TABLE DATA; Schema: public; Owner: dbuser
--



--
-- Data for Name: project_image; Type: TABLE DATA; Schema: public; Owner: dbuser
--



--
-- Data for Name: project_semantic_data; Type: TABLE DATA; Schema: public; Owner: dbuser
--

INSERT INTO public.project_semantic_data (id, project_id, semantic_data_id, name, created_on, last_modified_on) VALUES ('b8357031-510d-4584-ac13-a3634e15b7f6', '861bc7ba-defb-400c-982f-d6563f36af48', '38b7db6d-736d-45c3-ad08-d461774c83d9', 'main', '2024-06-07 09:37:50.664621+00', '2024-06-13 09:44:44.056725+00');


--
-- Data for Name: representation_metadata; Type: TABLE DATA; Schema: public; Owner: dbuser
--

INSERT INTO public.representation_metadata (id, representation_metadata_id, target_object_id, description_id, label, kind, created_on, last_modified_on, documentation, semantic_data_id) VALUES ('38b7db6d-736d-45c3-ad08-d461774c83d9#16d1cfa3-1200-47b5-8877-9b4ff1b273d3', '16d1cfa3-1200-47b5-8877-9b4ff1b273d3', '73f66477-a239-4dfe-88c9-bc99ee7a25ee', 'siriusComponents://representationDescription?kind=diagramDescription&sourceKind=view&sourceId=8dcd14b0-6259-3193-ad2c-743f394c68e4&sourceElementId=db495705-e917-319b-af55-a32ad63f4089', 'General View', 'siriusComponents://representation?type=Diagram', '2025-05-06 15:02:20.002612+00', '2025-05-06 15:02:20.002612+00', '', '38b7db6d-736d-45c3-ad08-d461774c83d9');


--
-- Data for Name: representation_content; Type: TABLE DATA; Schema: public; Owner: dbuser
--

INSERT INTO public.representation_content (id, representation_metadata_id, semantic_data_id, content, last_migration_performed, migration_version, created_on, last_modified_on) VALUES ('38b7db6d-736d-45c3-ad08-d461774c83d9#16d1cfa3-1200-47b5-8877-9b4ff1b273d3', '16d1cfa3-1200-47b5-8877-9b4ff1b273d3', '38b7db6d-736d-45c3-ad08-d461774c83d9', '{"id":"16d1cfa3-1200-47b5-8877-9b4ff1b273d3","kind":"siriusComponents://representation?type=Diagram","targetObjectId":"73f66477-a239-4dfe-88c9-bc99ee7a25ee","descriptionId":"siriusComponents://representationDescription?kind=diagramDescription&sourceKind=view&sourceId=8dcd14b0-6259-3193-ad2c-743f394c68e4&sourceElementId=db495705-e917-319b-af55-a32ad63f4089","nodes":[{"id":"ba18d350-9fd1-3c67-afdd-af35bd636671","type":"node:image","targetObjectId":"73f66477-a239-4dfe-88c9-bc99ee7a25ee","targetObjectKind":"siriusComponents://semantic?domain=sysml&entity=ViewUsage","targetObjectLabel":"General View","descriptionId":"siriusComponents://nodeDescription?sourceKind=view&sourceId=8dcd14b0-6259-3193-ad2c-743f394c68e4&sourceElementId=024ab54f-ab81-3d02-8abd-f65af641e6c6","borderNode":false,"modifiers":[],"state":"Normal","collapsingState":"EXPANDED","insideLabel":{"id":"b5f085b6-6479-34f9-a0ba-37ec9c2f4812","text":"","insideLabelLocation":"TOP_CENTER","style":{"color":"#000000","fontSize":14,"bold":false,"italic":false,"underline":false,"strikeThrough":false,"iconURL":[],"background":"transparent","borderColor":"black","borderSize":0,"borderRadius":3,"borderStyle":"Solid","maxWidth":null},"isHeader":false,"headerSeparatorDisplayMode":"NEVER","overflowStrategy":"NONE","textAlign":"LEFT"},"outsideLabels":[],"style":{"imageURL":"images/add_your_first_element.svg","scalingFactor":1,"borderColor":"transparent","borderSize":1,"borderRadius":0,"borderStyle":"Solid","positionDependentRotation":false},"childrenLayoutStrategy":null,"borderNodes":[],"childNodes":[],"defaultWidth":1061,"defaultHeight":476,"labelEditable":false,"pinned":false}],"edges":[],"layoutData":{"nodeLayoutData":{"ba18d350-9fd1-3c67-afdd-af35bd636671":{"id":"ba18d350-9fd1-3c67-afdd-af35bd636671","position":{"x":0.0,"y":0.0},"size":{"width":1061.0,"height":476.0},"resizedByUser":false,"handleLayoutData":[]}},"edgeLayoutData":{},"labelLayoutData":{}}}', 'none', '2025.4.0-202504011650', '2025-05-06 15:02:20.114471+00', '2025-05-06 15:02:21.359117+00');


--
-- Data for Name: semantic_data_dependency; Type: TABLE DATA; Schema: public; Owner: dbuser
--



--
-- Data for Name: semantic_data_domain; Type: TABLE DATA; Schema: public; Owner: dbuser
--

INSERT INTO public.semantic_data_domain (semantic_data_id, uri) VALUES ('38b7db6d-736d-45c3-ad08-d461774c83d9', 'http://www.eclipse.org/syson/sysml');


--
-- PostgreSQL database dump complete
--

