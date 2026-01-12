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

INSERT INTO public.semantic_data (id, created_on, last_modified_on) VALUES ('2ad408fb-f7b2-43cb-929a-e2b3d8cdf4af', '2025-05-22 13:31:39.192668+00', '2025-05-22 13:32:27.397728+00');


--
-- Data for Name: document; Type: TABLE DATA; Schema: public; Owner: dbuser
--

INSERT INTO public.document (id, semantic_data_id, name, content, is_read_only, created_on, last_modified_on) VALUES ('14a4121b-ec2e-41cd-99dd-c7612ba761bb', '2ad408fb-f7b2-43cb-929a-e2b3d8cdf4af', 'SysMLv2.sysml', '{"json":{"version":"1.0","encoding":"utf-8"},"ns":{"sysml":"http://www.eclipse.org/syson/sysml"},"migration":{"lastMigrationPerformed":"none","migrationVersion":"2025.6.0-202505150000"},"content":[{"id":"863ff0ea-5ae8-4029-b5c6-f57ad56b9013","eClass":"sysml:Namespace","data":{"elementId":"2a0c6bbc-eaac-464b-a447-c50900e29aa8","ownedRelationship":[{"id":"2fadbb89-3aed-4784-85bf-52b65d27fb6a","eClass":"sysml:OwningMembership","data":{"elementId":"f3769068-8094-4c29-b2a6-9ba0156b342f","ownedRelatedElement":[{"id":"1de6b808-b97b-44ba-87ca-0ed2d196a066","eClass":"sysml:Package","data":{"declaredName":"Package1","elementId":"f3d97864-09f0-4af7-95bd-44b1a3243fdb","ownedRelationship":[{"id":"38991b30-b569-4c7b-9c5c-37a7788192b1","eClass":"sysml:OwningMembership","data":{"elementId":"6ffe97d7-738b-4e4c-99e0-a89c34126d4e","ownedRelatedElement":[{"id":"578bf6dc-161f-444a-8209-e722a0dcbf55","eClass":"sysml:ViewUsage","data":{"declaredName":"General View","elementId":"b469efdb-1310-458e-a689-afd6e9f1702d","ownedRelationship":[{"id":"bc03640a-4795-428e-9bc8-2cecf647dcca","eClass":"sysml:FeatureTyping","data":{"elementId":"63076ba4-6551-4c48-bf8b-6302a503bf11","type":"sysml:ViewDefinition sysmllibrary:///faf517ae-dbcd-30a4-b3b9-3d9cb3bbf5c1#7098b764-d891-48f5-b928-4ba41cbe837e","typedFeature":"578bf6dc-161f-444a-8209-e722a0dcbf55"}}]}}]}},{"id":"0c3f37ad-69b2-4f20-92bf-10b28b0072de","eClass":"sysml:OwningMembership","data":{"elementId":"e015b9a7-6436-4664-b8c3-335b6838af88","ownedRelatedElement":[{"id":"8d3666ea-8d30-4008-81bb-58294282f884","eClass":"sysml:PartUsage","data":{"declaredName":"partA","elementId":"b2457be8-314c-4061-a307-54b79663790a","ownedRelationship":[{"id":"f18204cf-dc5d-490f-afeb-59c02cd33e36","eClass":"sysml:FeatureMembership","data":{"elementId":"468dbf69-80e1-4102-ad68-27c2d37f4770","ownedRelatedElement":[{"id":"de98a7e7-a042-4021-b0ba-79651cb4e420","eClass":"sysml:PartUsage","data":{"declaredName":"partB","elementId":"6209e9eb-446d-4b20-a90d-c211923b42f1","isComposite":true}}]}}],"isComposite":true}}]}}]}}]}}]}}]}', false, '2025-05-22 13:32:27.397721+00', '2025-05-22 13:32:27.397721+00');


--
-- Data for Name: image; Type: TABLE DATA; Schema: public; Owner: dbuser
--



--
-- Data for Name: library; Type: TABLE DATA; Schema: public; Owner: dbuser
--



--
-- Data for Name: project; Type: TABLE DATA; Schema: public; Owner: dbuser
--

INSERT INTO public.project (id, name, created_on, last_modified_on) VALUES ('5ec64279-b533-461d-bdb4-2ac5de399710', 'SysMLv2', '2025-05-22 13:31:39.124544+00', '2025-05-22 13:31:39.124544+00');


--
-- Data for Name: nature; Type: TABLE DATA; Schema: public; Owner: dbuser
--



--
-- Data for Name: project_image; Type: TABLE DATA; Schema: public; Owner: dbuser
--



--
-- Data for Name: project_semantic_data; Type: TABLE DATA; Schema: public; Owner: dbuser
--

INSERT INTO public.project_semantic_data (id, project_id, semantic_data_id, name, created_on, last_modified_on) VALUES ('1d7651b7-2feb-4e31-b901-e354578c8a91', '5ec64279-b533-461d-bdb4-2ac5de399710', '2ad408fb-f7b2-43cb-929a-e2b3d8cdf4af', 'main', '2025-05-22 13:31:39.203298+00', '2025-05-22 13:31:39.203298+00');


--
-- Data for Name: representation_metadata; Type: TABLE DATA; Schema: public; Owner: dbuser
--

INSERT INTO public.representation_metadata (id, representation_metadata_id, target_object_id, description_id, label, kind, created_on, last_modified_on, documentation, semantic_data_id) VALUES ('2ad408fb-f7b2-43cb-929a-e2b3d8cdf4af#6bd4c626-403b-410e-a5d4-c0db131fb2dd', '6bd4c626-403b-410e-a5d4-c0db131fb2dd', '578bf6dc-161f-444a-8209-e722a0dcbf55', 'siriusComponents://representationDescription?kind=diagramDescription&sourceKind=view&sourceId=8dcd14b0-6259-3193-ad2c-743f394c68e4&sourceElementId=db495705-e917-319b-af55-a32ad63f4089', 'General View', 'siriusComponents://representation?type=Diagram', '2025-05-22 13:31:39.976762+00', '2025-05-22 13:31:39.976762+00', '', '2ad408fb-f7b2-43cb-929a-e2b3d8cdf4af');


--
-- Data for Name: representation_content; Type: TABLE DATA; Schema: public; Owner: dbuser
--

INSERT INTO public.representation_content (id, representation_metadata_id, semantic_data_id, content, last_migration_performed, migration_version, created_on, last_modified_on) VALUES ('2ad408fb-f7b2-43cb-929a-e2b3d8cdf4af#6bd4c626-403b-410e-a5d4-c0db131fb2dd', '6bd4c626-403b-410e-a5d4-c0db131fb2dd', '2ad408fb-f7b2-43cb-929a-e2b3d8cdf4af', '{"id":"6bd4c626-403b-410e-a5d4-c0db131fb2dd","kind":"siriusComponents://representation?type=Diagram","targetObjectId":"578bf6dc-161f-444a-8209-e722a0dcbf55","descriptionId":"siriusComponents://representationDescription?kind=diagramDescription&sourceKind=view&sourceId=8dcd14b0-6259-3193-ad2c-743f394c68e4&sourceElementId=db495705-e917-319b-af55-a32ad63f4089","nodes":[{"id":"af61a8ab-5226-3406-8463-86d75bbce5b7","type":"node:image","targetObjectId":"578bf6dc-161f-444a-8209-e722a0dcbf55","targetObjectKind":"siriusComponents://semantic?domain=sysml&entity=ViewUsage","targetObjectLabel":"General View","descriptionId":"siriusComponents://nodeDescription?sourceKind=view&sourceId=8dcd14b0-6259-3193-ad2c-743f394c68e4&sourceElementId=024ab54f-ab81-3d02-8abd-f65af641e6c6","borderNode":false,"modifiers":[],"state":"Normal","collapsingState":"EXPANDED","insideLabel":{"id":"27ba5d01-370b-34a7-8b7f-cec57c703517","text":"","insideLabelLocation":"TOP_CENTER","style":{"color":"#000000","fontSize":14,"bold":false,"italic":false,"underline":false,"strikeThrough":false,"iconURL":[],"background":"transparent","borderColor":"black","borderSize":0,"borderRadius":3,"borderStyle":"Solid","maxWidth":null},"isHeader":false,"headerSeparatorDisplayMode":"NEVER","overflowStrategy":"NONE","textAlign":"LEFT"},"outsideLabels":[],"style":{"imageURL":"images/add_your_first_element.svg","scalingFactor":1,"borderColor":"transparent","borderSize":1,"borderRadius":0,"borderStyle":"Solid","positionDependentRotation":false},"childrenLayoutStrategy":null,"borderNodes":[],"childNodes":[],"defaultWidth":1061,"defaultHeight":476,"labelEditable":false,"pinned":false}],"edges":[],"layoutData":{"nodeLayoutData":{"af61a8ab-5226-3406-8463-86d75bbce5b7":{"id":"af61a8ab-5226-3406-8463-86d75bbce5b7","position":{"x":0.0,"y":0.0},"size":{"width":1061.0,"height":476.0},"resizedByUser":false,"handleLayoutData":[]}},"edgeLayoutData":{},"labelLayoutData":{}}}', 'none', '2025.6.0-202506011650', '2025-05-22 13:31:40.003743+00', '2025-05-22 13:31:40.793119+00');


--
-- Data for Name: semantic_data_dependency; Type: TABLE DATA; Schema: public; Owner: dbuser
--



--
-- Data for Name: semantic_data_domain; Type: TABLE DATA; Schema: public; Owner: dbuser
--

INSERT INTO public.semantic_data_domain (semantic_data_id, uri) VALUES ('2ad408fb-f7b2-43cb-929a-e2b3d8cdf4af', 'http://www.eclipse.org/syson/sysml');


--
-- PostgreSQL database dump complete
--

