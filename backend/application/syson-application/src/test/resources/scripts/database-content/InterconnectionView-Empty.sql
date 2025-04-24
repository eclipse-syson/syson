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

INSERT INTO public.semantic_data (id, created_on, last_modified_on) VALUES ('5e2327fa-1247-4429-9b26-64b7941965f2', '2024-10-21 15:33:48.430895+00', '2025-05-12 08:53:49.334441+00');


--
-- Data for Name: document; Type: TABLE DATA; Schema: public; Owner: dbuser
--

INSERT INTO public.document (id, semantic_data_id, name, content, created_on, last_modified_on) VALUES ('57fbb12e-e96e-4825-9fdf-07070b3f0228', '5e2327fa-1247-4429-9b26-64b7941965f2', 'SysMLv2.sysml', '{"json":{"version":"1.0","encoding":"utf-8"},"ns":{"sysml":"http://www.eclipse.org/syson/sysml"},"migration":{"lastMigrationPerformed":"EdgeDescriptionSourceTargetDescriptionsParticipant","migrationVersion":"2025.4.0-202503171117"},"content":[{"id":"d96a4a3d-6235-4399-bf18-89fae2938190","eClass":"sysml:Namespace","data":{"elementId":"e4ac9954-4969-4a23-816f-c66c28dd23d9","ownedRelationship":[{"id":"25f955bf-2a3d-4b7b-8e29-520106538d11","eClass":"sysml:OwningMembership","data":{"elementId":"a0f72204-5098-4bc7-aee9-3fb321b68a33","ownedRelatedElement":[{"id":"d272833b-ed73-4fe2-9a63-8e3009116a36","eClass":"sysml:Package","data":{"declaredName":"Package 1","elementId":"416f0c76-f9bf-4fb6-8734-842e4cd2fde2","ownedRelationship":[{"id":"9fe6bc84-dbfa-497e-91a9-cf71793ba790","eClass":"sysml:OwningMembership","data":{"elementId":"3f6c3644-2202-44bc-8cab-b77785bb3eb0","ownedRelatedElement":[{"id":"b06dee32-6645-4b78-8a86-8df3b4306c7f","eClass":"sysml:PartUsage","data":{"declaredName":"partDiagram","elementId":"6013478a-9fa6-438d-9804-a049a821735e","ownedRelationship":[{"id":"7c4c6291-33ce-4a77-aca8-8412db4ed0ff","eClass":"sysml:FeatureMembership","data":{"elementId":"2c9a2b0b-be56-460c-81cc-7e98f533e061","ownedRelatedElement":[{"id":"d4e910cc-db4f-4ee3-8065-5b3259383659","eClass":"sysml:ViewUsage","data":{"declaredName":"Interconnection View","elementId":"48040324-d928-490b-96ba-5cdb0762ce77","ownedRelationship":[{"id":"6aa2c714-f316-4104-b10b-6d8b5e86036a","eClass":"sysml:FeatureTyping","data":{"elementId":"d9372716-6a77-4873-a22f-b9d354235554","type":"sysml:ViewDefinition sysmllibrary:///faf517ae-dbcd-30a4-b3b9-3d9cb3bbf5c1#6518462a-2f51-5276-b95e-69ee5193db38","typedFeature":"48040324-d928-490b-96ba-5cdb0762ce77"}}]}}]}}],"isComposite":true}}]}}]}}]}}]}}]}', '2025-05-12 08:53:49.334256+00', '2025-05-12 08:53:49.334256+00');


--
-- Data for Name: image; Type: TABLE DATA; Schema: public; Owner: dbuser
--



--
-- Data for Name: library; Type: TABLE DATA; Schema: public; Owner: dbuser
--



--
-- Data for Name: project; Type: TABLE DATA; Schema: public; Owner: dbuser
--

INSERT INTO public.project (id, name, created_on, last_modified_on) VALUES ('43df3d33-5cad-447b-a2fb-a31a5d096d92', 'InterconnectionView-Empty', '2024-10-21 15:33:48.12165+00', '2024-10-21 15:34:51.199351+00');


--
-- Data for Name: nature; Type: TABLE DATA; Schema: public; Owner: dbuser
--



--
-- Data for Name: project_image; Type: TABLE DATA; Schema: public; Owner: dbuser
--



--
-- Data for Name: project_semantic_data; Type: TABLE DATA; Schema: public; Owner: dbuser
--

INSERT INTO public.project_semantic_data (id, project_id, semantic_data_id, name, created_on, last_modified_on) VALUES ('3151242c-1173-430b-9153-64c4bede576a', '43df3d33-5cad-447b-a2fb-a31a5d096d92', '5e2327fa-1247-4429-9b26-64b7941965f2', 'main', '2024-10-21 15:33:48.430895+00', '2024-10-21 15:35:43.573574+00');


--
-- Data for Name: representation_metadata; Type: TABLE DATA; Schema: public; Owner: dbuser
--

INSERT INTO public.representation_metadata (id, target_object_id, description_id, label, kind, created_on, last_modified_on, documentation, semantic_data_id) VALUES ('7133c6bc-bc52-4242-b394-232fd0f67e17', 'd4e910cc-db4f-4ee3-8065-5b3259383659', 'siriusComponents://representationDescription?kind=diagramDescription&sourceKind=view&sourceId=74c5d045-51d7-359f-9634-611d0f1bef3d&sourceElementId=e1bd3b6d-357b-3068-b2e9-e0c1e19d6856', 'Interconnection View', 'siriusComponents://representation?type=Diagram', '2025-05-12 08:53:49.088517+00', '2025-05-12 08:53:49.088517+00', '', '5e2327fa-1247-4429-9b26-64b7941965f2');


--
-- Data for Name: representation_content; Type: TABLE DATA; Schema: public; Owner: dbuser
--

INSERT INTO public.representation_content (id, content, last_migration_performed, migration_version, created_on, last_modified_on) VALUES ('7133c6bc-bc52-4242-b394-232fd0f67e17', '{"id":"7133c6bc-bc52-4242-b394-232fd0f67e17","kind":"siriusComponents://representation?type=Diagram","targetObjectId":"d4e910cc-db4f-4ee3-8065-5b3259383659","descriptionId":"siriusComponents://representationDescription?kind=diagramDescription&sourceKind=view&sourceId=74c5d045-51d7-359f-9634-611d0f1bef3d&sourceElementId=e1bd3b6d-357b-3068-b2e9-e0c1e19d6856","nodes":[{"id":"15f9ed6f-bfa7-34aa-991c-d7b44f72c8f3","type":"node:rectangle","targetObjectId":"b06dee32-6645-4b78-8a86-8df3b4306c7f","targetObjectKind":"siriusComponents://semantic?domain=sysml&entity=PartUsage","targetObjectLabel":"partDiagram","descriptionId":"siriusComponents://nodeDescription?sourceKind=view&sourceId=74c5d045-51d7-359f-9634-611d0f1bef3d&sourceElementId=4055a909-560d-3db5-a777-ea29c14cc2c2","borderNode":false,"modifiers":[],"state":"Normal","collapsingState":"EXPANDED","insideLabel":{"id":"f61a7789-dd44-37d1-b27c-a2fff42a0681","text":"«part»\npartDiagram","insideLabelLocation":"TOP_CENTER","style":{"color":"#000000","fontSize":14,"bold":false,"italic":false,"underline":false,"strikeThrough":false,"iconURL":["/icons/full/obj16/PartUsage.svg"],"background":"transparent","borderColor":"black","borderSize":0,"borderRadius":3,"borderStyle":"Solid","maxWidth":null},"isHeader":true,"headerSeparatorDisplayMode":"NEVER","overflowStrategy":"NONE","textAlign":"CENTER"},"outsideLabels":[],"style":{"background":"#ffffff","borderColor":"#000000","borderSize":1,"borderRadius":10,"borderStyle":"Solid"},"childrenLayoutStrategy":{"kind":"FreeForm"},"borderNodes":[],"childNodes":[],"defaultWidth":700,"defaultHeight":400,"labelEditable":true,"pinned":false}],"edges":[],"layoutData":{"nodeLayoutData":{"15f9ed6f-bfa7-34aa-991c-d7b44f72c8f3":{"id":"15f9ed6f-bfa7-34aa-991c-d7b44f72c8f3","position":{"x":0.0,"y":0.0},"size":{"width":700.0,"height":400.0},"resizedByUser":false,"handleLayoutData":[]}},"edgeLayoutData":{},"labelLayoutData":{}}}', 'none', '2025.4.0-202504011650', '2025-05-12 08:53:49.209902+00', '2025-05-12 08:58:45.4239+00');


--
-- Data for Name: semantic_data_dependency; Type: TABLE DATA; Schema: public; Owner: dbuser
--



--
-- Data for Name: semantic_data_domain; Type: TABLE DATA; Schema: public; Owner: dbuser
--

INSERT INTO public.semantic_data_domain (semantic_data_id, uri) VALUES ('5e2327fa-1247-4429-9b26-64b7941965f2', 'http://www.eclipse.org/syson/sysml');


--
-- PostgreSQL database dump complete
--

