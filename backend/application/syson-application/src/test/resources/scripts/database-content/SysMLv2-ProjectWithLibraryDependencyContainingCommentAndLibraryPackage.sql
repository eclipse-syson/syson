--
-- PostgreSQL database dump
--

-- Dumped from database version 12.18 (Debian 12.18-1.pgdg120+2)
-- Dumped by pg_dump version 16.3

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

INSERT INTO public.semantic_data (id, created_on, last_modified_on) VALUES ('732e2e1a-04b5-4539-a60a-3fee14c033d8', '2025-11-26 13:23:11.65674+00', '2025-11-26 13:23:11.65674+00');
INSERT INTO public.semantic_data (id, created_on, last_modified_on) VALUES ('00fdbaac-73bc-4188-8758-0070e47b993c', '2025-11-26 13:23:14.677287+00', '2025-11-26 13:23:26.324326+00');


--
-- Data for Name: document; Type: TABLE DATA; Schema: public; Owner: dbuser
--

INSERT INTO public.document (id, semantic_data_id, name, content, created_on, last_modified_on, is_read_only) VALUES ('aaf38015-c4a4-49db-b40d-552e066f94bf', '732e2e1a-04b5-4539-a60a-3fee14c033d8', 'LibraryWithOneCommentAndOneLibraryPackage.sysml', '{"json":{"version":"1.0","encoding":"utf-8"},"ns":{"sysml":"http://www.eclipse.org/syson/sysml"},"content":[{"id":"7128adaa-62e1-4f24-8445-daeac1da4853","eClass":"sysml:Namespace","data":{"elementId":"8621a2ca-cdc3-4346-be74-9647e3f80ca6","ownedRelationship":[{"id":"055a0674-4bf5-4ec6-be54-b34fb9803d1a","eClass":"sysml:OwningMembership","data":{"elementId":"83bf8c5e-a124-4fe7-bd0b-7a22f3cb2170","ownedRelatedElement":[{"id":"dac75aa5-f468-47c5-9106-93ac48990725","eClass":"sysml:Comment","data":{"declaredName":"Comment1","elementId":"4c77daff-ad5a-40b7-b5b0-6138e3ae7664","body":"add comment here"}}]}},{"id":"4c189c11-8ae4-4946-92a2-8b8b103fe034","eClass":"sysml:OwningMembership","data":{"elementId":"ac9a598c-0384-486c-9525-f95145593bfd","ownedRelatedElement":[{"id":"3a12d098-04a8-4640-a46d-eef763f2ca60","eClass":"sysml:LibraryPackage","data":{"declaredName":"LibraryPackage1","elementId":"d37f4a66-2433-47eb-8df6-49d2d3f3f4ac","ownedRelationship":[{"id":"3d4484c9-10db-4b4e-84b1-d39ccb11eb6c","eClass":"sysml:OwningMembership","data":{"elementId":"5d96532f-b7a5-4dc8-b059-b0629c10d641","ownedRelatedElement":[{"id":"b91bcccb-82c1-4b64-909d-6794d65aec5f","eClass":"sysml:PartUsage","data":{"declaredName":"part1","elementId":"06098dd2-4fa2-4791-b9ff-a9eaf69156b6","isComposite":true}}]}}]}}]}}]}}]}', '2025-11-26 13:23:11.654232+00', '2025-11-26 13:23:11.654232+00', false);
INSERT INTO public.document (id, semantic_data_id, name, content, created_on, last_modified_on, is_read_only) VALUES ('b5208907-fc56-4821-9361-518c3eb14ed0', '00fdbaac-73bc-4188-8758-0070e47b993c', 'SysMLv2.sysml', '{"json":{"version":"1.0","encoding":"utf-8"},"ns":{"sysml":"http://www.eclipse.org/syson/sysml"},"migration":{"lastMigrationPerformed":"none","migrationVersion":"2025.8.0-202508220000"},"content":[{"id":"5b73a2cc-7eda-4db2-bd50-236892031392","eClass":"sysml:Namespace","data":{"elementId":"f27927dd-b067-46c3-80fb-bc9381c47b48","ownedRelationship":[{"id":"f1a124cb-7e4f-408b-8d62-f07bbea22099","eClass":"sysml:OwningMembership","data":{"elementId":"670c75d0-ef34-48f7-a72a-133acb6a3409","ownedRelatedElement":[{"id":"7a16c132-e1ce-4065-bd43-ffbb853e6dc6","eClass":"sysml:Package","data":{"declaredName":"Package1","elementId":"9e95fc90-9b89-430d-9adf-e4a3cdf1ac08","ownedRelationship":[{"id":"3539995b-db19-4b95-aaed-996f100ec4e0","eClass":"sysml:OwningMembership","data":{"elementId":"b25eadb3-4857-423e-ac5d-6ec709bf18a0","ownedRelatedElement":[{"id":"d9acc8a3-b0dc-4f76-b14f-bffe54e14eec","eClass":"sysml:ViewUsage","data":{"declaredName":"view1","elementId":"bad2412a-6bbf-4904-96ff-8c7946965a20","ownedRelationship":[{"id":"16675a3f-edb6-4ee8-b01e-57dc3aaf1ebe","eClass":"sysml:FeatureTyping","data":{"elementId":"ef6db6ac-e5f7-4774-bb72-f3549e9a2a34","type":"sysml:ViewDefinition sysmllibrary:///faf517ae-dbcd-30a4-b3b9-3d9cb3bbf5c1#03904fdf-d6f2-57b1-92d5-95d36b8208dc","typedFeature":"d9acc8a3-b0dc-4f76-b14f-bffe54e14eec"}}]}}]}}]}}]}}]}}]}', '2025-11-26 13:23:15.704577+00', '2025-11-26 13:23:15.704577+00', false);


--
-- Data for Name: image; Type: TABLE DATA; Schema: public; Owner: dbuser
--



--
-- Data for Name: library; Type: TABLE DATA; Schema: public; Owner: dbuser
--

INSERT INTO public.library (id, namespace, name, version, semantic_data_id, description, created_on, last_modified_on) VALUES ('25f2429f-1947-48fa-852a-084fbeb77802', 'b3b3ad8e-dae7-468a-955f-e9914491c911', 'LibraryWithOneCommentAndOneLibraryPackage', '1.0.0', '732e2e1a-04b5-4539-a60a-3fee14c033d8', '', '2025-11-26 13:23:11.702431+00', '2025-11-26 13:23:11.702431+00');


--
-- Data for Name: project; Type: TABLE DATA; Schema: public; Owner: dbuser
--

INSERT INTO public.project (id, name, created_on, last_modified_on) VALUES ('be5101e5-27ac-41bc-b050-cb9c87ed4bc7', 'SysMLv2', '2025-11-26 13:23:14.63964+00', '2025-11-26 13:23:14.63964+00');


--
-- Data for Name: nature; Type: TABLE DATA; Schema: public; Owner: dbuser
--



--
-- Data for Name: project_image; Type: TABLE DATA; Schema: public; Owner: dbuser
--



--
-- Data for Name: project_semantic_data; Type: TABLE DATA; Schema: public; Owner: dbuser
--

INSERT INTO public.project_semantic_data (id, project_id, semantic_data_id, name, created_on, last_modified_on) VALUES ('682cb941-1790-44ff-96f1-76501c14e3ed', 'be5101e5-27ac-41bc-b050-cb9c87ed4bc7', '00fdbaac-73bc-4188-8758-0070e47b993c', 'main', '2025-11-26 13:23:14.713348+00', '2025-11-26 13:23:14.713348+00');


--
-- Data for Name: representation_metadata; Type: TABLE DATA; Schema: public; Owner: dbuser
--

INSERT INTO public.representation_metadata (id, representation_metadata_id, target_object_id, description_id, label, kind, created_on, last_modified_on, documentation, semantic_data_id) VALUES ('00fdbaac-73bc-4188-8758-0070e47b993c#7361537e-a8f2-492f-bad3-67db2a46699d', '7361537e-a8f2-492f-bad3-67db2a46699d', 'd9acc8a3-b0dc-4f76-b14f-bffe54e14eec', 'siriusComponents://representationDescription?kind=diagramDescription&sourceKind=view&sourceId=8dcd14b0-6259-3193-ad2c-743f394c68e4&sourceElementId=db495705-e917-319b-af55-a32ad63f4089', 'view1', 'siriusComponents://representation?type=Diagram', '2025-11-26 13:23:15.632027+00', '2025-11-26 13:23:15.632027+00', '', '00fdbaac-73bc-4188-8758-0070e47b993c');


--
-- Data for Name: representation_content; Type: TABLE DATA; Schema: public; Owner: dbuser
--

INSERT INTO public.representation_content (id, representation_metadata_id, semantic_data_id, content, last_migration_performed, migration_version, created_on, last_modified_on) VALUES ('00fdbaac-73bc-4188-8758-0070e47b993c#7361537e-a8f2-492f-bad3-67db2a46699d', '7361537e-a8f2-492f-bad3-67db2a46699d', '00fdbaac-73bc-4188-8758-0070e47b993c', '{"id":"7361537e-a8f2-492f-bad3-67db2a46699d","kind":"siriusComponents://representation?type=Diagram","targetObjectId":"d9acc8a3-b0dc-4f76-b14f-bffe54e14eec","descriptionId":"siriusComponents://representationDescription?kind=diagramDescription&sourceKind=view&sourceId=8dcd14b0-6259-3193-ad2c-743f394c68e4&sourceElementId=db495705-e917-319b-af55-a32ad63f4089","nodes":[{"id":"7a3af033-d1ad-386a-abac-c4f9f2a2ed84","type":"node:image","targetObjectId":"d9acc8a3-b0dc-4f76-b14f-bffe54e14eec","targetObjectKind":"siriusComponents://semantic?domain=sysml&entity=ViewUsage","targetObjectLabel":"view1","descriptionId":"siriusComponents://nodeDescription?sourceKind=view&sourceId=8dcd14b0-6259-3193-ad2c-743f394c68e4&sourceElementId=024ab54f-ab81-3d02-8abd-f65af641e6c6","borderNode":false,"initialBorderNodePosition":"NONE","modifiers":[],"state":"Normal","collapsingState":"EXPANDED","insideLabel":{"id":"1e8981ae-4431-39e6-9933-b155ba1384a8","text":"","insideLabelLocation":"TOP_CENTER","style":{"color":"#000000","fontSize":14,"bold":false,"italic":false,"underline":false,"strikeThrough":false,"iconURL":[],"background":"transparent","borderColor":"black","borderSize":0,"borderRadius":3,"borderStyle":"Solid","maxWidth":null,"visibility":"visible"},"isHeader":false,"headerSeparatorDisplayMode":"NEVER","overflowStrategy":"NONE","textAlign":"LEFT","customizedStyleProperties":[]},"outsideLabels":[],"style":{"imageURL":"images/add_your_first_element.svg","scalingFactor":1,"borderColor":"transparent","borderSize":1,"borderRadius":0,"borderStyle":"Solid","positionDependentRotation":false,"childrenLayoutStrategy":{"kind":"FreeForm"}},"borderNodes":[],"childNodes":[],"defaultWidth":1061,"defaultHeight":476,"labelEditable":false,"pinned":false,"customizedStyleProperties":[]}],"edges":[],"layoutData":{"nodeLayoutData":{"7a3af033-d1ad-386a-abac-c4f9f2a2ed84":{"id":"7a3af033-d1ad-386a-abac-c4f9f2a2ed84","position":{"x":0.0,"y":0.0},"size":{"width":1061.0,"height":476.0},"resizedByUser":false,"movedByUser":false,"handleLayoutData":[]}},"edgeLayoutData":{},"labelLayoutData":{}}}', 'none', '2025.8.0-202506301700', '2025-11-26 13:23:15.668903+00', '2025-11-26 13:23:17.205768+00');


--
-- Data for Name: semantic_data_dependency; Type: TABLE DATA; Schema: public; Owner: dbuser
--

INSERT INTO public.semantic_data_dependency (semantic_data_id, dependency_semantic_data_id, index) VALUES ('00fdbaac-73bc-4188-8758-0070e47b993c', '732e2e1a-04b5-4539-a60a-3fee14c033d8', 0);


--
-- Data for Name: semantic_data_domain; Type: TABLE DATA; Schema: public; Owner: dbuser
--

INSERT INTO public.semantic_data_domain (semantic_data_id, uri) VALUES ('732e2e1a-04b5-4539-a60a-3fee14c033d8', 'http://www.eclipse.org/syson/sysml');
INSERT INTO public.semantic_data_domain (semantic_data_id, uri) VALUES ('00fdbaac-73bc-4188-8758-0070e47b993c', 'http://www.eclipse.org/syson/sysml');


--
-- PostgreSQL database dump complete
--

