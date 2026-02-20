--
-- PostgreSQL database dump
--

-- Dumped from database version 15.15 (Debian 15.15-1.pgdg13+1)
-- Dumped by pg_dump version 18.2

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

INSERT INTO public.semantic_data (id, created_on, last_modified_on) VALUES ('089fc654-a72b-43da-8117-caeacc0f5f9b', '2026-02-19 15:03:02.139893+00', '2026-02-19 15:16:02.886154+00');


--
-- Data for Name: document; Type: TABLE DATA; Schema: public; Owner: dbuser
--

INSERT INTO public.document (id, semantic_data_id, name, content, created_on, last_modified_on, is_read_only) VALUES ('91015e21-160b-4996-b4e7-8e2be921f814', '089fc654-a72b-43da-8117-caeacc0f5f9b', 'SysMLv2.sysml', '{"json":{"version":"1.0","encoding":"utf-8"},"ns":{"sysml":"http://www.eclipse.org/syson/sysml"},"migration":{"lastMigrationPerformed":"none","migrationVersion":"2026.3.0-202602171555"},"content":[{"id":"3948640c-2ddc-443e-8a4e-abf16b71ee33","eClass":"sysml:Namespace","data":{"elementId":"a89a1715-0bfd-4035-bd9c-9ff73f863b3d","ownedRelationship":[{"id":"b4eb5792-f3a2-404d-8e1b-04a908fb63c4","eClass":"sysml:OwningMembership","data":{"elementId":"f3427f7d-35ac-4c0d-a610-f0bf4911eb21","ownedRelatedElement":[{"id":"0388995b-3e9a-4f11-a915-1f8792b53690","eClass":"sysml:Package","data":{"declaredName":"Package1","elementId":"a088c392-f7cf-485e-9237-8953fbe1ad7c","ownedRelationship":[{"id":"a4166dce-cf4f-49e1-8151-ffd7fb25089d","eClass":"sysml:OwningMembership","data":{"elementId":"80d3d6f4-88b5-47f7-9877-a726f165a787","ownedRelatedElement":[{"id":"20a54630-ce64-4e08-a171-20b9f945ffdc","eClass":"sysml:ViewUsage","data":{"declaredName":"view1","elementId":"5ecf341c-09f8-4543-ab11-94ee707d73c9","ownedRelationship":[{"id":"216652b5-6fcf-4bde-9ec2-dfbb063112c2","eClass":"sysml:FeatureTyping","data":{"elementId":"e36939aa-7d4b-468c-b0d2-cdcc9d9a5f67","type":"sysml:ViewDefinition sysmllibrary:///faf517ae-dbcd-30a4-b3b9-3d9cb3bbf5c1#03904fdf-d6f2-57b1-92d5-95d36b8208dc","typedFeature":"20a54630-ce64-4e08-a171-20b9f945ffdc"}},{"id":"2d72194f-6c43-4b50-84ba-aec0f15aac83","eClass":"sysml:MembershipExpose","data":{"elementId":"15b8ba86-a609-4701-8386-e7cadf860d61","isImportAll":true,"visibility":"protected","importedMembership":"9dfcc97f-8273-4969-bfb2-21fdc1ca01c2"}}]}}]}},{"id":"9dfcc97f-8273-4969-bfb2-21fdc1ca01c2","eClass":"sysml:OwningMembership","data":{"elementId":"19903a90-7d30-4769-9b93-19dd30163fd1","ownedRelatedElement":[{"id":"5e778a4e-dd84-43a0-80c5-5c14c4ea96a7","eClass":"sysml:ViewUsage","data":{"declaredName":"view2","elementId":"618a1639-1456-408c-86f2-21316a5c9b3f","ownedRelationship":[{"id":"ead98e3c-ea20-4c63-b0e3-04235f055086","eClass":"sysml:FeatureTyping","data":{"elementId":"1da82d40-1da0-4a37-92c8-7f9a4a66a1be","type":"sysml:ViewDefinition sysmllibrary:///faf517ae-dbcd-30a4-b3b9-3d9cb3bbf5c1#03904fdf-d6f2-57b1-92d5-95d36b8208dc","typedFeature":"5e778a4e-dd84-43a0-80c5-5c14c4ea96a7"}},{"id":"99d9e637-6458-4f7a-82e1-0d3df6b21648","eClass":"sysml:MembershipExpose","data":{"elementId":"3602218f-1f25-4a65-86ac-6f2c8cf75a15","isImportAll":true,"visibility":"protected","importedMembership":"caa764fe-4dbd-431d-b29a-4052f3aea6c8"}}],"isComposite":true}}]}},{"id":"caa764fe-4dbd-431d-b29a-4052f3aea6c8","eClass":"sysml:OwningMembership","data":{"elementId":"b03bc819-99e7-40dc-902d-f1e2c9fe2e84","ownedRelatedElement":[{"id":"8d885c6b-49d2-4eae-b50d-5250cf56e5e1","eClass":"sysml:ViewUsage","data":{"declaredName":"view3","elementId":"95a08e2c-1a55-4c23-a1d9-52aebe1cb604","ownedRelationship":[{"id":"eab58e50-a07b-4162-902d-3c7a477d44a6","eClass":"sysml:FeatureTyping","data":{"elementId":"b1d9840c-d471-4216-9f59-28c37b3d0fce","type":"sysml:ViewDefinition sysmllibrary:///faf517ae-dbcd-30a4-b3b9-3d9cb3bbf5c1#03904fdf-d6f2-57b1-92d5-95d36b8208dc","typedFeature":"8d885c6b-49d2-4eae-b50d-5250cf56e5e1"}}],"isComposite":true}}]}}]}}]}}]}}]}', '2026-02-19 15:16:02.885988+00', '2026-02-19 15:16:02.885988+00', false);


--
-- Data for Name: image; Type: TABLE DATA; Schema: public; Owner: dbuser
--



--
-- Data for Name: library; Type: TABLE DATA; Schema: public; Owner: dbuser
--



--
-- Data for Name: project; Type: TABLE DATA; Schema: public; Owner: dbuser
--

INSERT INTO public.project (id, name, created_on, last_modified_on) VALUES ('b6dba677-b551-4ec9-ae28-20e159b265e4', 'SysMLv2', '2026-02-19 15:02:57.585035+00', '2026-02-19 15:02:57.585035+00');


--
-- Data for Name: nature; Type: TABLE DATA; Schema: public; Owner: dbuser
--



--
-- Data for Name: project_image; Type: TABLE DATA; Schema: public; Owner: dbuser
--



--
-- Data for Name: project_semantic_data; Type: TABLE DATA; Schema: public; Owner: dbuser
--

INSERT INTO public.project_semantic_data (id, project_id, semantic_data_id, name, created_on, last_modified_on) VALUES ('f557a4ee-1014-4c73-bd72-af98141379cd', 'b6dba677-b551-4ec9-ae28-20e159b265e4', '089fc654-a72b-43da-8117-caeacc0f5f9b', 'main', '2026-02-19 15:03:02.23132+00', '2026-02-19 15:03:02.23132+00');


--
-- Data for Name: representation_metadata; Type: TABLE DATA; Schema: public; Owner: dbuser
--

INSERT INTO public.representation_metadata (id, target_object_id, description_id, label, kind, created_on, last_modified_on, documentation, semantic_data_id, representation_metadata_id) VALUES ('089fc654-a72b-43da-8117-caeacc0f5f9b#a7abf1bd-5208-48da-b870-9afe66487964', '20a54630-ce64-4e08-a171-20b9f945ffdc', 'siriusComponents://representationDescription?kind=diagramDescription&sourceKind=view&sourceId=8dcd14b0-6259-3193-ad2c-743f394c68e4&sourceElementId=db495705-e917-319b-af55-a32ad63f4089', 'view1', 'siriusComponents://representation?type=Diagram', '2026-02-19 15:03:03.144412+00', '2026-02-19 15:03:03.144412+00', '', '089fc654-a72b-43da-8117-caeacc0f5f9b', 'a7abf1bd-5208-48da-b870-9afe66487964');
INSERT INTO public.representation_metadata (id, target_object_id, description_id, label, kind, created_on, last_modified_on, documentation, semantic_data_id, representation_metadata_id) VALUES ('089fc654-a72b-43da-8117-caeacc0f5f9b#35d3d921-af78-43a7-b4d3-83734524c5d3', '5e778a4e-dd84-43a0-80c5-5c14c4ea96a7', 'siriusComponents://representationDescription?kind=diagramDescription&sourceKind=view&sourceId=8dcd14b0-6259-3193-ad2c-743f394c68e4&sourceElementId=db495705-e917-319b-af55-a32ad63f4089', 'view2', 'siriusComponents://representation?type=Diagram', '2026-02-19 15:03:15.886223+00', '2026-02-19 15:03:15.886223+00', '', '089fc654-a72b-43da-8117-caeacc0f5f9b', '35d3d921-af78-43a7-b4d3-83734524c5d3');


--
-- Data for Name: representation_content; Type: TABLE DATA; Schema: public; Owner: dbuser
--

INSERT INTO public.representation_content (id, content, last_migration_performed, migration_version, created_on, last_modified_on, semantic_data_id, representation_metadata_id) VALUES ('089fc654-a72b-43da-8117-caeacc0f5f9b#35d3d921-af78-43a7-b4d3-83734524c5d3', '{"id":"35d3d921-af78-43a7-b4d3-83734524c5d3","kind":"siriusComponents://representation?type=Diagram","targetObjectId":"5e778a4e-dd84-43a0-80c5-5c14c4ea96a7","descriptionId":"siriusComponents://representationDescription?kind=diagramDescription&sourceKind=view&sourceId=8dcd14b0-6259-3193-ad2c-743f394c68e4&sourceElementId=db495705-e917-319b-af55-a32ad63f4089","nodes":[{"id":"3c97bc8a-326a-32f5-b4bb-40ccd8abebf4","type":"node:image","targetObjectId":"5e778a4e-dd84-43a0-80c5-5c14c4ea96a7","targetObjectKind":"siriusComponents://semantic?domain=sysml&entity=ViewUsage","targetObjectLabel":"view2","descriptionId":"siriusComponents://nodeDescription?sourceKind=view&sourceId=8dcd14b0-6259-3193-ad2c-743f394c68e4&sourceElementId=024ab54f-ab81-3d02-8abd-f65af641e6c6","borderNode":false,"initialBorderNodePosition":"NONE","modifiers":[],"state":"Normal","collapsingState":"EXPANDED","insideLabel":{"id":"99af692e-e3fc-35e6-963b-7ff55452155e","text":"","insideLabelLocation":"TOP_CENTER","style":{"color":"#000000","fontSize":14,"bold":false,"italic":false,"underline":false,"strikeThrough":false,"iconURL":[],"background":"transparent","borderColor":"black","borderSize":0,"borderRadius":3,"borderStyle":"Solid","maxWidth":null,"visibility":"visible"},"isHeader":false,"headerSeparatorDisplayMode":"NEVER","overflowStrategy":"NONE","textAlign":"LEFT","customizedStyleProperties":[]},"outsideLabels":[],"style":{"imageURL":"images/add_your_first_element.svg","scalingFactor":1,"borderColor":"transparent","borderSize":1,"borderRadius":0,"borderStyle":"Solid","positionDependentRotation":false,"childrenLayoutStrategy":{"kind":"FreeForm"}},"borderNodes":[],"childNodes":[],"defaultWidth":1061,"defaultHeight":476,"labelEditable":false,"deletable":false,"pinned":false,"customizedStyleProperties":[]}],"edges":[],"layoutData":{"nodeLayoutData":{},"edgeLayoutData":{},"labelLayoutData":{}}}', 'none', '2025.12.0-202511141745', '2026-02-19 15:03:15.895721+00', '2026-02-19 15:03:15.895721+00', '089fc654-a72b-43da-8117-caeacc0f5f9b', '35d3d921-af78-43a7-b4d3-83734524c5d3');
INSERT INTO public.representation_content (id, content, last_migration_performed, migration_version, created_on, last_modified_on, semantic_data_id, representation_metadata_id) VALUES ('089fc654-a72b-43da-8117-caeacc0f5f9b#a7abf1bd-5208-48da-b870-9afe66487964', '{"id":"a7abf1bd-5208-48da-b870-9afe66487964","kind":"siriusComponents://representation?type=Diagram","targetObjectId":"20a54630-ce64-4e08-a171-20b9f945ffdc","descriptionId":"siriusComponents://representationDescription?kind=diagramDescription&sourceKind=view&sourceId=8dcd14b0-6259-3193-ad2c-743f394c68e4&sourceElementId=db495705-e917-319b-af55-a32ad63f4089","nodes":[{"id":"4ec4159e-9a38-3f40-b9d8-7c40a212a818","type":"customnode:sysmlviewframe","targetObjectId":"5e778a4e-dd84-43a0-80c5-5c14c4ea96a7","targetObjectKind":"siriusComponents://semantic?domain=sysml&entity=ViewUsage","targetObjectLabel":"view2","descriptionId":"siriusComponents://nodeDescription?sourceKind=view&sourceId=8dcd14b0-6259-3193-ad2c-743f394c68e4&sourceElementId=7001fab9-43ec-356c-8dad-26574a6b2d53","borderNode":false,"initialBorderNodePosition":"NONE","modifiers":[],"state":"Normal","collapsingState":"EXPANDED","insideLabel":{"id":"140e665a-599c-31c8-9b36-e3de9cadbbfe","text":"«view» view2 : StandardViewDefinitions::GeneralView","insideLabelLocation":"TOP_CENTER","style":{"color":"#000000","fontSize":14,"bold":false,"italic":false,"underline":false,"strikeThrough":false,"iconURL":["/icons/full/obj16/ViewUsage.svg"],"background":"transparent","borderColor":"black","borderSize":0,"borderRadius":3,"borderStyle":"Solid","maxWidth":null,"visibility":"visible"},"isHeader":false,"headerSeparatorDisplayMode":"NEVER","overflowStrategy":"ELLIPSIS","textAlign":"CENTER","customizedStyleProperties":[]},"outsideLabels":[],"style":{"background":"#ffffff","borderColor":"#000000","borderSize":1,"borderStyle":"Solid","borderRadius":10,"childrenLayoutStrategy":{"kind":"FreeForm"}},"borderNodes":[],"childNodes":[{"id":"2b1584ea-dde1-3596-931d-40af44dbee69","type":"customnode:sysmlviewframe","targetObjectId":"8d885c6b-49d2-4eae-b50d-5250cf56e5e1","targetObjectKind":"siriusComponents://semantic?domain=sysml&entity=ViewUsage","targetObjectLabel":"view3","descriptionId":"siriusComponents://nodeDescription?sourceKind=view&sourceId=8dcd14b0-6259-3193-ad2c-743f394c68e4&sourceElementId=7001fab9-43ec-356c-8dad-26574a6b2d53","borderNode":false,"initialBorderNodePosition":"NONE","modifiers":[],"state":"Normal","collapsingState":"EXPANDED","insideLabel":{"id":"862408df-a8c7-3423-bd50-d993566ee049","text":"«view» view3 : StandardViewDefinitions::GeneralView","insideLabelLocation":"TOP_CENTER","style":{"color":"#000000","fontSize":14,"bold":false,"italic":false,"underline":false,"strikeThrough":false,"iconURL":["/icons/full/obj16/ViewUsage.svg"],"background":"transparent","borderColor":"black","borderSize":0,"borderRadius":3,"borderStyle":"Solid","maxWidth":null,"visibility":"visible"},"isHeader":false,"headerSeparatorDisplayMode":"NEVER","overflowStrategy":"ELLIPSIS","textAlign":"CENTER","customizedStyleProperties":[]},"outsideLabels":[],"style":{"background":"#ffffff","borderColor":"#000000","borderSize":1,"borderStyle":"Solid","borderRadius":10,"childrenLayoutStrategy":{"kind":"FreeForm"}},"borderNodes":[],"childNodes":[],"defaultWidth":200,"defaultHeight":101,"labelEditable":true,"deletable":true,"pinned":false,"customizedStyleProperties":[]}],"defaultWidth":200,"defaultHeight":101,"labelEditable":true,"deletable":true,"pinned":false,"customizedStyleProperties":[]}],"edges":[],"layoutData":{"nodeLayoutData":{"2b1584ea-dde1-3596-931d-40af44dbee69":{"id":"2b1584ea-dde1-3596-931d-40af44dbee69","position":{"x":78.08029280598964,"y":81.61657826741538},"size":{"width":200.0,"height":101.0},"resizedByUser":false,"movedByUser":false,"handleLayoutData":[],"minComputedSize":{"width":0.0,"height":0.0}},"4ec4159e-9a38-3f40-b9d8-7c40a212a818":{"id":"4ec4159e-9a38-3f40-b9d8-7c40a212a818","position":{"x":160.16385595703125,"y":-219.73897633870445},"size":{"width":361.0,"height":287.0},"resizedByUser":true,"movedByUser":false,"handleLayoutData":[],"minComputedSize":{"width":0.0,"height":0.0}}},"edgeLayoutData":{},"labelLayoutData":{}}}', 'none', '2025.12.0-202511141745', '2026-02-19 15:03:03.229774+00', '2026-02-19 15:16:02.947681+00', '089fc654-a72b-43da-8117-caeacc0f5f9b', 'a7abf1bd-5208-48da-b870-9afe66487964');


--
-- Data for Name: semantic_data_dependency; Type: TABLE DATA; Schema: public; Owner: dbuser
--



--
-- Data for Name: semantic_data_domain; Type: TABLE DATA; Schema: public; Owner: dbuser
--

INSERT INTO public.semantic_data_domain (semantic_data_id, uri) VALUES ('089fc654-a72b-43da-8117-caeacc0f5f9b', 'http://www.eclipse.org/syson/sysml');


--
-- PostgreSQL database dump complete
--

