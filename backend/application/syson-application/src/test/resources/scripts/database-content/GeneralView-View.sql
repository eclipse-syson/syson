--
-- PostgreSQL database dump
--

-- Dumped from database version 17.5 (Debian 17.5-1.pgdg120+1)
-- Dumped by pg_dump version 17.5

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

INSERT INTO public.semantic_data (id, created_on, last_modified_on) VALUES ('493f6a98-3416-44bb-a21b-d8ef9b237564', '2025-05-12 09:14:59.790332+00', '2025-07-23 08:33:28.82693+00');


--
-- Data for Name: document; Type: TABLE DATA; Schema: public; Owner: dbuser
--

INSERT INTO public.document (id, semantic_data_id, name, content, created_on, last_modified_on) VALUES ('32d35705-7907-4b58-b15f-30c0a9e06a9f', '493f6a98-3416-44bb-a21b-d8ef9b237564', 'GeneralView-View.sysml', '{"json":{"version":"1.0","encoding":"utf-8"},"ns":{"sysml":"http://www.eclipse.org/syson/sysml"},"migration":{"lastMigrationPerformed":"StandardLibrariesElementsReferencesMigrationParticipant","migrationVersion":"2025.8.0-202506301600"},"content":[{"id":"67569384-a182-458e-87ec-f728d9ff1d43","eClass":"sysml:Namespace","data":{"elementId":"c644b718-4577-4d4a-823b-eb326eee279d","ownedRelationship":[{"id":"ea17cf02-befd-4bfd-b674-3fa81348e6cd","eClass":"sysml:OwningMembership","data":{"elementId":"719c2dc4-5c90-41be-b309-4776d711b111","ownedRelatedElement":[{"id":"351775b8-5dc8-4c08-9f71-a12fdcb0d616","eClass":"sysml:Package","data":{"declaredName":"Package 1","elementId":"23145475-eb37-4198-9863-c6e8b891ce8f","ownedRelationship":[{"id":"c82666f0-dd91-4cbc-9db2-7e2ea733c344","eClass":"sysml:OwningMembership","data":{"elementId":"eae8bc8e-39d1-407b-a3af-857b75103762","ownedRelatedElement":[{"id":"31966893-cd7e-4440-84e0-5389a8e90b5c","eClass":"sysml:ViewUsage","data":{"declaredName":"view1","elementId":"fcad1f2c-4284-420c-8da2-856eb084c480","ownedRelationship":[{"id":"6614d4b8-05df-4d9e-9477-ed0d81c35450","eClass":"sysml:FeatureTyping","data":{"elementId":"f99fb282-0d82-4c19-8866-04bfbcdbd3df","type":"sysml:ViewDefinition sysmllibrary:///faf517ae-dbcd-30a4-b3b9-3d9cb3bbf5c1#03904fdf-d6f2-57b1-92d5-95d36b8208dc","typedFeature":"31966893-cd7e-4440-84e0-5389a8e90b5c"}}],"isComposite":true}}]}},{"id":"37b02c8c-d000-40ad-ab9a-263f643382ba","eClass":"sysml:OwningMembership","data":{"elementId":"eef8a652-fd23-428e-8998-1338907375b1","ownedRelatedElement":[{"id":"853216c1-ee16-4cdb-8e78-b3966bf42285","eClass":"sysml:ViewUsage","data":{"declaredName":"General View","elementId":"5b15beee-dd05-4e04-baaf-dd54bba448bd","ownedRelationship":[{"id":"0034b5f0-a5c8-4745-850a-3b6236f3c507","eClass":"sysml:FeatureTyping","data":{"elementId":"10fe0473-2429-4a08-92f5-9ded1115909b","type":"sysml:ViewDefinition sysmllibrary:///faf517ae-dbcd-30a4-b3b9-3d9cb3bbf5c1#03904fdf-d6f2-57b1-92d5-95d36b8208dc","typedFeature":"853216c1-ee16-4cdb-8e78-b3966bf42285"}},{"id":"05967974-7853-4240-a713-0a9b581f51c9","eClass":"sysml:MembershipExpose","data":{"elementId":"531cd8f4-90b8-4ea4-9c41-e001eddb4612","importedMembership":"c82666f0-dd91-4cbc-9db2-7e2ea733c344"}}]}}]}}]}}]}}]}}]}', '2025-07-23 08:33:28.826874+00', '2025-07-23 08:33:28.826874+00');


--
-- Data for Name: image; Type: TABLE DATA; Schema: public; Owner: dbuser
--



--
-- Data for Name: library; Type: TABLE DATA; Schema: public; Owner: dbuser
--



--
-- Data for Name: project; Type: TABLE DATA; Schema: public; Owner: dbuser
--

INSERT INTO public.project (id, name, created_on, last_modified_on) VALUES ('c4db1e1e-1908-458b-a346-a796922de1e3', 'GeneralView-View', '2025-05-12 09:14:59.723398+00', '2025-05-12 09:15:27.967092+00');


--
-- Data for Name: nature; Type: TABLE DATA; Schema: public; Owner: dbuser
--



--
-- Data for Name: project_image; Type: TABLE DATA; Schema: public; Owner: dbuser
--



--
-- Data for Name: project_semantic_data; Type: TABLE DATA; Schema: public; Owner: dbuser
--

INSERT INTO public.project_semantic_data (id, project_id, semantic_data_id, name, created_on, last_modified_on) VALUES ('ef724eee-b604-4df1-a993-01ef4e99ffbf', 'c4db1e1e-1908-458b-a346-a796922de1e3', '493f6a98-3416-44bb-a21b-d8ef9b237564', 'main', '2025-05-12 09:14:59.815923+00', '2025-05-12 09:14:59.815923+00');


--
-- Data for Name: representation_metadata; Type: TABLE DATA; Schema: public; Owner: dbuser
--

INSERT INTO public.representation_metadata (id, target_object_id, description_id, label, kind, created_on, last_modified_on, documentation, semantic_data_id) VALUES ('ee82558c-f54e-4c1d-924e-b6a85469bcd7', '853216c1-ee16-4cdb-8e78-b3966bf42285', 'siriusComponents://representationDescription?kind=diagramDescription&sourceKind=view&sourceId=8dcd14b0-6259-3193-ad2c-743f394c68e4&sourceElementId=db495705-e917-319b-af55-a32ad63f4089', 'General View', 'siriusComponents://representation?type=Diagram', '2025-05-12 09:15:02.459444+00', '2025-07-23 08:33:09.749069+00', '', '493f6a98-3416-44bb-a21b-d8ef9b237564');


--
-- Data for Name: representation_content; Type: TABLE DATA; Schema: public; Owner: dbuser
--

INSERT INTO public.representation_content (id, content, last_migration_performed, migration_version, created_on, last_modified_on) VALUES ('ee82558c-f54e-4c1d-924e-b6a85469bcd7', '{"id":"ee82558c-f54e-4c1d-924e-b6a85469bcd7","kind":"siriusComponents://representation?type=Diagram","targetObjectId":"853216c1-ee16-4cdb-8e78-b3966bf42285","descriptionId":"siriusComponents://representationDescription?kind=diagramDescription&sourceKind=view&sourceId=8dcd14b0-6259-3193-ad2c-743f394c68e4&sourceElementId=db495705-e917-319b-af55-a32ad63f4089","nodes":[{"id":"cd5e4319-6045-3280-b3df-d7373bf1afd8","type":"customnode:sysmlviewframe","targetObjectId":"31966893-cd7e-4440-84e0-5389a8e90b5c","targetObjectKind":"siriusComponents://semantic?domain=sysml&entity=ViewUsage","targetObjectLabel":"view1","descriptionId":"siriusComponents://nodeDescription?sourceKind=view&sourceId=8dcd14b0-6259-3193-ad2c-743f394c68e4&sourceElementId=7001fab9-43ec-356c-8dad-26574a6b2d53","borderNode":false,"modifiers":[],"state":"Normal","collapsingState":"EXPANDED","insideLabel":{"id":"25266d4a-8a0e-3004-9d3d-6a5b9d5bb4a6","text":"«view» view1 : StandardViewDefinitions::GeneralView","insideLabelLocation":"TOP_CENTER","style":{"color":"#000000","fontSize":14,"bold":false,"italic":false,"underline":false,"strikeThrough":false,"iconURL":["/icons/full/obj16/ViewUsage.svg"],"background":"transparent","borderColor":"black","borderSize":0,"borderRadius":3,"borderStyle":"Solid","maxWidth":null},"isHeader":false,"headerSeparatorDisplayMode":"NEVER","overflowStrategy":"ELLIPSIS","textAlign":"CENTER","customizedStyleProperties":[]},"outsideLabels":[],"style":{"background":"#ffffff","borderColor":"#000000","borderSize":1,"borderStyle":"Solid","borderRadius":10,"childrenLayoutStrategy":{"kind":"FreeForm"}},"borderNodes":[],"childNodes":[],"defaultWidth":300,"defaultHeight":101,"labelEditable":true,"pinned":false,"customizedStyleProperties":[]}],"edges":[],"layoutData":{"nodeLayoutData":{"cd5e4319-6045-3280-b3df-d7373bf1afd8":{"id":"cd5e4319-6045-3280-b3df-d7373bf1afd8","position":{"x":70.60683358433738,"y":-233.7552920013386},"size":{"width":920.0,"height":738.0},"resizedByUser":true,"handleLayoutData":[]}},"edgeLayoutData":{},"labelLayoutData":{}}}', 'StandardLibrariesElementsDiagramMigrationParticipant', '2025.8.0-202506301700', '2025-05-12 09:15:02.51973+00', '2025-07-23 08:33:34.120793+00');


--
-- Data for Name: semantic_data_dependency; Type: TABLE DATA; Schema: public; Owner: dbuser
--



--
-- Data for Name: semantic_data_domain; Type: TABLE DATA; Schema: public; Owner: dbuser
--

INSERT INTO public.semantic_data_domain (semantic_data_id, uri) VALUES ('493f6a98-3416-44bb-a21b-d8ef9b237564', 'http://www.eclipse.org/syson/sysml');


--
-- PostgreSQL database dump complete
--

