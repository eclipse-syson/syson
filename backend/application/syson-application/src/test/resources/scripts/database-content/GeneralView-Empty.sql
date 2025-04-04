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

INSERT INTO public.semantic_data (id, created_on, last_modified_on) VALUES ('38b7db6d-736d-45c3-ad08-d461774c83d9', '2024-06-07 09:37:50.664621+00', '2024-06-13 09:44:44.056725+00');


--
-- Data for Name: document; Type: TABLE DATA; Schema: public; Owner: dbuser
--

INSERT INTO public.document (id, semantic_data_id, name, content, created_on, last_modified_on) VALUES ('82b6c2a2-43ab-4a8d-baa6-920652fd963f', '38b7db6d-736d-45c3-ad08-d461774c83d9', 'SysMLv2', '{"json":{"version":"1.0","encoding":"utf-8"},"ns":{"sysml":"http://www.eclipse.org/syson/sysml"},"migration":{"lastMigrationPerformed":"none","migrationVersion":"2024.5.0-202405061500"},"content":[{"id":"3c8831a2-9759-40da-a6a2-4ef4f5939136","eClass":"sysml:Namespace","data":{"elementId":"e8745a10-90d3-43ce-9b17-f04abbf7a82c","ownedRelationship":[{"id":"4ed5fcdb-e177-44ed-b056-cb0fdf232089","eClass":"sysml:OwningMembership","data":{"elementId":"413cfbcd-6e75-4a12-806d-e453cb963471","ownedRelatedElement":[{"id":"528fae42-60c2-40c8-baa3-684d874a950d","eClass":"sysml:Package","data":{"declaredName":"Package 1","elementId":"b7050480-08ca-4536-8d52-3c7b74e88f19"}}]}}]}}]}', '2024-06-13 09:44:44.056725+00', '2024-06-13 09:44:44.056725+00');


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

INSERT INTO public.representation_metadata (id, target_object_id, description_id, label, kind, created_on, last_modified_on, documentation, semantic_data_id) VALUES ('e92f36f4-48ea-4a57-89b1-0acf98f86f6d', '528fae42-60c2-40c8-baa3-684d874a950d', 'siriusComponents://representationDescription?kind=diagramDescription&sourceKind=view&sourceId=8dcd14b0-6259-3193-ad2c-743f394c68e4&sourceElementId=db495705-e917-319b-af55-a32ad63f4089', 'General View', 'siriusComponents://representation?type=Diagram', '2024-01-01 09:42:00+00', '2024-01-02 09:42:00+00', '', '38b7db6d-736d-45c3-ad08-d461774c83d9');


--
-- Data for Name: representation_content; Type: TABLE DATA; Schema: public; Owner: dbuser
--

INSERT INTO public.representation_content (id, content, last_migration_performed, migration_version, created_on, last_modified_on) VALUES ('e92f36f4-48ea-4a57-89b1-0acf98f86f6d', '{"id":"e92f36f4-48ea-4a57-89b1-0acf98f86f6d","kind":"siriusComponents://representation?type=Diagram","targetObjectId":"528fae42-60c2-40c8-baa3-684d874a950d","descriptionId":"siriusComponents://representationDescription?kind=diagramDescription&sourceKind=view&sourceId=8dcd14b0-6259-3193-ad2c-743f394c68e4&sourceElementId=db495705-e917-319b-af55-a32ad63f4089","label":"General View","position":{"x":-1.0,"y":-1.0},"size":{"width":-1.0,"height":-1.0},"nodes":[{"id":"26a98442-63d9-38d7-967e-06130be13f0c","type":"node:image","targetObjectId":"528fae42-60c2-40c8-baa3-684d874a950d","targetObjectKind":"siriusComponents://semantic?domain=sysml&entity=Package","targetObjectLabel":"Package 1","descriptionId":"siriusComponents://nodeDescription?sourceKind=view&sourceId=8dcd14b0-6259-3193-ad2c-743f394c68e4&sourceElementId=024ab54f-ab81-3d02-8abd-f65af641e6c6","borderNode":false,"modifiers":[],"state":"Normal","collapsingState":"EXPANDED","insideLabel":{"id":"0c2a8280-d453-3df3-bbc9-ed61e3a98f26","text":"","insideLabelLocation":"TOP_CENTER","style":{"color":"#000000","fontSize":14,"bold":false,"italic":false,"underline":false,"strikeThrough":false,"iconURL":[],"background":"transparent","borderColor":"black","borderSize":0,"borderRadius":3,"borderStyle":"Solid"},"isHeader":false,"displayHeaderSeparator":false,"overflowStrategy":"NONE","textAlign":"LEFT"},"outsideLabels":[],"style":{"imageURL":"images/add_your_first_element.svg","scalingFactor":1,"borderColor":"transparent","borderSize":1,"borderRadius":0,"borderStyle":"Solid","positionDependentRotation":false},"childrenLayoutStrategy":null,"position":{"x":-1.0,"y":-1.0},"size":{"width":1061.0,"height":476.0},"borderNodes":[],"childNodes":[],"customizedProperties":[],"defaultWidth":1061,"defaultHeight":476,"labelEditable":false,"pinned":false}],"edges":[],"layoutData":{"nodeLayoutData":{"26a98442-63d9-38d7-967e-06130be13f0c":{"id":"26a98442-63d9-38d7-967e-06130be13f0c","position":{"x":0.0,"y":0.0},"size":{"width":1061.0,"height":476.0},"resizedByUser":false}},"edgeLayoutData":{},"labelLayoutData":{}}}', 'none', '2024.5.4-202407040900', '2024-01-01 09:42:00+00', '2024-01-02 09:42:00+00');


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

