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

INSERT INTO public.semantic_data (id, created_on, last_modified_on) VALUES ('ddee6b42-cb86-4809-b1fb-e431c6263b9f', '2025-11-05 10:34:59.55945+00', '2025-11-05 10:34:59.55945+00');
INSERT INTO public.semantic_data (id, created_on, last_modified_on) VALUES ('9fcdc1e3-c28a-472e-97ac-27c9bcf87890', '2025-11-05 10:35:02.956379+00', '2025-11-05 10:35:15.436249+00');


--
-- Data for Name: document; Type: TABLE DATA; Schema: public; Owner: dbuser
--

INSERT INTO public.document (id, semantic_data_id, name, content, created_on, last_modified_on, is_read_only) VALUES ('07dad64a-ac60-4348-b828-402f98427cb9', 'ddee6b42-cb86-4809-b1fb-e431c6263b9f', 'Library.sysml', '{"json":{"version":"1.0","encoding":"utf-8"},"ns":{"sysml":"http://www.eclipse.org/syson/sysml"},"content":[{"id":"10f1cdec-f938-4cbf-a884-0e8223d5b08a","eClass":"sysml:Namespace","data":{"elementId":"ad78ee14-ffa0-4b12-9cb5-366cb36c154b","ownedRelationship":[{"id":"1125b8ac-b3f3-462a-8a75-e0fcec2b9522","eClass":"sysml:OwningMembership","data":{"elementId":"fa623b29-0ec2-4cf6-9954-1c5f7e5ac157","ownedRelatedElement":[{"id":"1a24b4e4-89d4-4c5f-af71-d76661c0f827","eClass":"sysml:LibraryPackage","data":{"declaredName":"LibraryPackage","elementId":"4049b77a-c2ab-4235-b618-888330f3851e","ownedRelationship":[{"id":"1859cce9-ab53-4829-a747-ada0db8748cb","eClass":"sysml:OwningMembership","data":{"elementId":"2560dbe3-4f82-464b-8588-3e3ef0f72f78","ownedRelatedElement":[{"id":"351e4263-1402-4ec9-b997-3bfd2c445b5b","eClass":"sysml:PartUsage","data":{"declaredName":"part1","elementId":"0a7ef2be-48d6-42b7-9f71-4a55d5359290","isComposite":true}}]}}]}}]}},{"id":"02970ef5-11d5-469a-9ea9-2d801357264e","eClass":"sysml:OwningMembership","data":{"elementId":"4dc2ab75-8740-48ec-800e-352f14dccf18","ownedRelatedElement":[{"id":"7578bcc0-febd-4e95-8a51-f98b5685d9dd","eClass":"sysml:Package","data":{"declaredName":"Package","elementId":"c081f015-72ac-4b32-9f06-e03e70f96708","ownedRelationship":[{"id":"b64d7343-c1fd-4b00-95f0-6c2828a6192f","eClass":"sysml:OwningMembership","data":{"elementId":"c38e8262-c8ed-4c8c-950d-d70716e4014c","ownedRelatedElement":[{"id":"f74c4f44-7367-4bab-9af9-6e95d5da9998","eClass":"sysml:PartUsage","data":{"declaredName":"part2","elementId":"39cd203e-743d-4814-be63-78c403b30bc1","isComposite":true}}]}}]}}]}}]}}]}', '2025-11-05 10:34:59.554514+00', '2025-11-05 10:34:59.554514+00', false);
INSERT INTO public.document (id, semantic_data_id, name, content, created_on, last_modified_on, is_read_only) VALUES ('21917d0e-ef24-411a-9951-030093be6fdb', '9fcdc1e3-c28a-472e-97ac-27c9bcf87890', 'SysMLv2.sysml', '{"json":{"version":"1.0","encoding":"utf-8"},"ns":{"sysml":"http://www.eclipse.org/syson/sysml"},"migration":{"lastMigrationPerformed":"none","migrationVersion":"2025.8.0-202508220000"},"content":[{"id":"68a16d3f-d344-487b-b45f-331c36ab9fd8","eClass":"sysml:Namespace","data":{"elementId":"2dc7fac2-4d26-449f-9090-9892efd873fa","ownedRelationship":[{"id":"ceb6cdf7-7d62-4139-b796-172c4c1d9ed8","eClass":"sysml:OwningMembership","data":{"elementId":"0f5a7dc9-9932-4add-9749-04625c8995fe","ownedRelatedElement":[{"id":"02a4dcc3-629e-4a99-9bdb-af9d87eabbae","eClass":"sysml:Package","data":{"declaredName":"Package1","elementId":"bb2db624-04ab-4a57-bd86-08994005fd77","ownedRelationship":[{"id":"71ca3f39-6dd6-4eb0-9701-5a76fa1689b6","eClass":"sysml:OwningMembership","data":{"elementId":"42bdf8c0-1460-430b-abb5-b0e8ff97792e","ownedRelatedElement":[{"id":"bf70555c-c0ba-408f-a621-eb3f4c399213","eClass":"sysml:ViewUsage","data":{"declaredName":"view1","elementId":"072fd4f6-4498-4b21-b3db-c2be83ada547","ownedRelationship":[{"id":"0956b092-58f0-42c6-8908-2adc82949cab","eClass":"sysml:FeatureTyping","data":{"elementId":"94024d5a-4919-4563-9f19-a02ed456ae1a","type":"sysml:ViewDefinition sysmllibrary:///faf517ae-dbcd-30a4-b3b9-3d9cb3bbf5c1#03904fdf-d6f2-57b1-92d5-95d36b8208dc","typedFeature":"bf70555c-c0ba-408f-a621-eb3f4c399213"}}]}}]}}]}}]}}]}}]}', '2025-11-05 10:35:05.414096+00', '2025-11-05 10:35:05.414096+00', false);


--
-- Data for Name: image; Type: TABLE DATA; Schema: public; Owner: dbuser
--



--
-- Data for Name: library; Type: TABLE DATA; Schema: public; Owner: dbuser
--

INSERT INTO public.library (id, namespace, name, version, semantic_data_id, description, created_on, last_modified_on) VALUES ('d7eaffdc-33c3-4865-8d5b-0e00ba1d20a6', 'd3ef270b-3914-4f34-a46b-e22feb3230fa', 'LibraryWithOnePackageAndOneLibraryPackage', '1.0.0', 'ddee6b42-cb86-4809-b1fb-e431c6263b9f', '', '2025-11-05 10:34:59.615123+00', '2025-11-05 10:34:59.615123+00');


--
-- Data for Name: project; Type: TABLE DATA; Schema: public; Owner: dbuser
--

INSERT INTO public.project (id, name, created_on, last_modified_on) VALUES ('43004111-b61d-40bb-9f1a-2f147476674d', 'SysMLv2', '2025-11-05 10:35:02.926078+00', '2025-11-05 10:35:02.926078+00');


--
-- Data for Name: nature; Type: TABLE DATA; Schema: public; Owner: dbuser
--



--
-- Data for Name: project_image; Type: TABLE DATA; Schema: public; Owner: dbuser
--



--
-- Data for Name: project_semantic_data; Type: TABLE DATA; Schema: public; Owner: dbuser
--

INSERT INTO public.project_semantic_data (id, project_id, semantic_data_id, name, created_on, last_modified_on) VALUES ('b04c9375-f0ed-4d0a-80a9-7ae222cf2a00', '43004111-b61d-40bb-9f1a-2f147476674d', '9fcdc1e3-c28a-472e-97ac-27c9bcf87890', 'main', '2025-11-05 10:35:02.981143+00', '2025-11-05 10:35:02.981143+00');


--
-- Data for Name: representation_metadata; Type: TABLE DATA; Schema: public; Owner: dbuser
--

INSERT INTO public.representation_metadata (id, target_object_id, description_id, label, kind, created_on, last_modified_on, documentation, semantic_data_id) VALUES ('a1b89c15-a33d-46b4-9561-37dc185f4a26', 'bf70555c-c0ba-408f-a621-eb3f4c399213', 'siriusComponents://representationDescription?kind=diagramDescription&sourceKind=view&sourceId=8dcd14b0-6259-3193-ad2c-743f394c68e4&sourceElementId=db495705-e917-319b-af55-a32ad63f4089', 'view1', 'siriusComponents://representation?type=Diagram', '2025-11-05 10:35:05.362916+00', '2025-11-05 10:35:05.362916+00', '', '9fcdc1e3-c28a-472e-97ac-27c9bcf87890');


--
-- Data for Name: representation_content; Type: TABLE DATA; Schema: public; Owner: dbuser
--

INSERT INTO public.representation_content (id, content, last_migration_performed, migration_version, created_on, last_modified_on) VALUES ('a1b89c15-a33d-46b4-9561-37dc185f4a26', '{"id":"a1b89c15-a33d-46b4-9561-37dc185f4a26","kind":"siriusComponents://representation?type=Diagram","targetObjectId":"bf70555c-c0ba-408f-a621-eb3f4c399213","descriptionId":"siriusComponents://representationDescription?kind=diagramDescription&sourceKind=view&sourceId=8dcd14b0-6259-3193-ad2c-743f394c68e4&sourceElementId=db495705-e917-319b-af55-a32ad63f4089","nodes":[{"id":"2e052fbe-28d4-3d38-a118-71eb3884423e","type":"node:image","targetObjectId":"bf70555c-c0ba-408f-a621-eb3f4c399213","targetObjectKind":"siriusComponents://semantic?domain=sysml&entity=ViewUsage","targetObjectLabel":"view1","descriptionId":"siriusComponents://nodeDescription?sourceKind=view&sourceId=8dcd14b0-6259-3193-ad2c-743f394c68e4&sourceElementId=024ab54f-ab81-3d02-8abd-f65af641e6c6","borderNode":false,"initialBorderNodePosition":"NONE","modifiers":[],"state":"Normal","collapsingState":"EXPANDED","insideLabel":{"id":"0df5dc33-964b-37e0-87bc-54a5e535a9db","text":"","insideLabelLocation":"TOP_CENTER","style":{"color":"#000000","fontSize":14,"bold":false,"italic":false,"underline":false,"strikeThrough":false,"iconURL":[],"background":"transparent","borderColor":"black","borderSize":0,"borderRadius":3,"borderStyle":"Solid","maxWidth":null,"visibility":"visible"},"isHeader":false,"headerSeparatorDisplayMode":"NEVER","overflowStrategy":"NONE","textAlign":"LEFT","customizedStyleProperties":[]},"outsideLabels":[],"style":{"imageURL":"images/add_your_first_element.svg","scalingFactor":1,"borderColor":"transparent","borderSize":1,"borderRadius":0,"borderStyle":"Solid","positionDependentRotation":false,"childrenLayoutStrategy":{"kind":"FreeForm"}},"borderNodes":[],"childNodes":[],"defaultWidth":1061,"defaultHeight":476,"labelEditable":false,"pinned":false,"customizedStyleProperties":[]}],"edges":[],"layoutData":{"nodeLayoutData":{"2e052fbe-28d4-3d38-a118-71eb3884423e":{"id":"2e052fbe-28d4-3d38-a118-71eb3884423e","position":{"x":0.0,"y":0.0},"size":{"width":1061.0,"height":476.0},"resizedByUser":false,"handleLayoutData":[]}},"edgeLayoutData":{},"labelLayoutData":{}}}', 'none', '2025.8.0-202506301700', '2025-11-05 10:35:05.390461+00', '2025-11-05 10:35:08.32129+00');


--
-- Data for Name: semantic_data_dependency; Type: TABLE DATA; Schema: public; Owner: dbuser
--

INSERT INTO public.semantic_data_dependency (semantic_data_id, dependency_semantic_data_id, index) VALUES ('9fcdc1e3-c28a-472e-97ac-27c9bcf87890', 'ddee6b42-cb86-4809-b1fb-e431c6263b9f', 0);


--
-- Data for Name: semantic_data_domain; Type: TABLE DATA; Schema: public; Owner: dbuser
--

INSERT INTO public.semantic_data_domain (semantic_data_id, uri) VALUES ('ddee6b42-cb86-4809-b1fb-e431c6263b9f', 'http://www.eclipse.org/syson/sysml');
INSERT INTO public.semantic_data_domain (semantic_data_id, uri) VALUES ('9fcdc1e3-c28a-472e-97ac-27c9bcf87890', 'http://www.eclipse.org/syson/sysml');


--
-- PostgreSQL database dump complete
--

