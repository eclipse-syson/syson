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

INSERT INTO public.semantic_data (id, created_on, last_modified_on) VALUES ('cbb4b3b4-af8e-4a05-a419-19dbfd1cc94d', '2024-07-05 10:58:27.155597+00', '2025-03-20 06:43:04.735581+00');


--
-- Data for Name: document; Type: TABLE DATA; Schema: public; Owner: dbuser
--

INSERT INTO public.document (id, semantic_data_id, name, content, created_on, last_modified_on) VALUES ('24b1e71e-7b8c-4931-b29d-66001dbb750d', 'cbb4b3b4-af8e-4a05-a419-19dbfd1cc94d', 'SysMLv2', '{"json":{"version":"1.0","encoding":"utf-8"},"ns":{"sysml":"http://www.eclipse.org/syson/sysml"},"migration":{"lastMigrationPerformed":"AnnotationAnnotatingElementMigrationParticipant","migrationVersion":"2025.4.0-202503140000"},"content":[{"id":"beb26e85-9513-449a-9e7a-613018574b58","eClass":"sysml:Namespace","data":{"elementId":"032c2526-e962-4c04-aabd-9b7db7b4a594","ownedRelationship":[{"id":"5843cc7a-1b80-4d60-ad66-7782df3ccc29","eClass":"sysml:OwningMembership","data":{"elementId":"476f6776-7fcb-47b0-a312-e5cb1f3db994","ownedRelatedElement":[{"id":"8d4123ac-3ac5-412d-90f2-49282b923003","eClass":"sysml:Package","data":{"declaredName":"Package 1","elementId":"051033ae-99bc-4ecd-9867-24eb0ca0887d","ownedRelationship":[{"id":"07e1f0f2-a64e-4fc6-b972-f1502afa196e","eClass":"sysml:OwningMembership","data":{"elementId":"d0cc11f0-fb87-44e5-bb81-7a6ef4cc8b56","ownedRelatedElement":[{"id":"a389db6f-3dab-4e2a-b33f-44edbfa3265a","eClass":"sysml:PartUsage","data":{"declaredName":"part1","elementId":"67a57df8-2995-41ea-a838-dfb3a9a8ee7f","ownedRelationship":[{"id":"e945eaf7-bcdd-4028-be8e-ab5e0e4bbc82","eClass":"sysml:FeatureMembership","data":{"elementId":"802c4e9d-a4c7-484e-8f19-9d15dc85cee9","ownedRelatedElement":[{"id":"99a90b90-b42c-4743-a262-d35736eb68ff","eClass":"sysml:PartUsage","data":{"declaredName":"part2","elementId":"120a2e65-d3b9-4100-8cb5-7ea5a2eefa8b"}}]}}]}}]}},{"id":"fb89a316-9e01-47fb-87fe-0f1f2b9179ca","eClass":"sysml:OwningMembership","data":{"elementId":"6b76e73d-abb6-4847-b3d9-3515f172de6c","ownedRelatedElement":[{"id":"40d86f92-e400-413d-8ec6-09fcf179b396","eClass":"sysml:ActionUsage","data":{"declaredName":"action1","elementId":"9b6d8cfc-1552-4d8a-8e4e-a75fc1dc8394","ownedRelationship":[{"id":"97717deb-89d8-4bd4-8124-f4c9118656ad","eClass":"sysml:FeatureMembership","data":{"elementId":"4cc4b978-de98-40ed-9138-fe71b0aa3652","ownedRelatedElement":[{"id":"f0a37795-69e1-4ffe-becc-e55c20c70ac3","eClass":"sysml:ActionUsage","data":{"declaredName":"action2","elementId":"a859d209-5064-4dc9-aa8f-fb70ca876e92","ownedRelationship":[{"id":"0280a924-653a-4ca5-868e-ad57ba4d659e","eClass":"sysml:FeatureMembership","data":{"elementId":"d731ff33-a6a3-48ad-86f8-74cecabd2d2f","ownedRelatedElement":[{"id":"cbf8b68e-b7f1-477e-8593-41ab43c74b98","eClass":"sysml:ActionUsage","data":{"declaredName":"action3","elementId":"7a1b2602-7db7-4476-b5d2-b13369d6c206"}}]}}]}}]}},{"id":"9303133f-5aeb-4c65-9a4d-ad692ce6a713","eClass":"sysml:Membership","data":{"elementId":"13b99c86-70d3-47e7-8594-f8130b0dd163","memberElement":"sysml:ActionUsage sysmllibrary:///ea54fd17-52d6-3366-82aa-494d0678e42d#9a0d2905-0f9c-5bb4-af74-9780d6db1817"}},{"id":"7cbb37e0-09c0-4252-baed-5ee3981ecd25","eClass":"sysml:FeatureMembership","data":{"elementId":"68822a02-bae7-4379-a515-8365d74c197b","ownedRelatedElement":[{"id":"18d6f2fc-2182-4217-8da2-bf68c36ffa41","eClass":"sysml:SuccessionAsUsage","data":{"elementId":"21da661c-7107-4b04-831b-4f447b1cf797","ownedRelationship":[{"id":"bb0006fa-bf7f-4f72-bc8a-158cb2d4dfa3","eClass":"sysml:EndFeatureMembership","data":{"elementId":"216588c4-a2c5-464c-8169-4c7803a90cd6","ownedRelatedElement":[{"id":"64d83589-8830-47d9-987e-5c0eedb917bc","eClass":"sysml:ReferenceUsage","data":{"aliasIds":["13b99c86-70d3-47e7-8594-f8130b0dd163"],"elementId":"804df1f6-523f-4674-80c3-319f55b26ba3","ownedRelationship":[{"id":"420d4203-cb2e-4a1a-979d-6b62069206a4","eClass":"sysml:ReferenceSubsetting","data":{"elementId":"fd89625e-1652-4943-9034-5ac556147fbb","referencedFeature":"sysml:ActionUsage sysmllibrary:///ea54fd17-52d6-3366-82aa-494d0678e42d#9a0d2905-0f9c-5bb4-af74-9780d6db1817"}}],"isEnd":true}}]}},{"id":"03d75ca9-fd67-4bfa-9956-37b6673552a1","eClass":"sysml:EndFeatureMembership","data":{"elementId":"ff3d0423-8050-4eef-9c80-cd1275aa6411","ownedRelatedElement":[{"id":"7020c45e-b0e1-4b43-a986-b7df35913873","eClass":"sysml:ReferenceUsage","data":{"elementId":"5cff679d-be5a-4e3c-ae3e-127e33f365c8","ownedRelationship":[{"id":"4ae4c1eb-7ccb-4c16-b31a-d5b3a0ba08aa","eClass":"sysml:ReferenceSubsetting","data":{"elementId":"658a6a8d-003a-4cbb-a911-97a5328f04c8","referencedFeature":"a859d209-5064-4dc9-aa8f-fb70ca876e92"}}],"isEnd":true}}]}}]}}]}}]}}]}},{"id":"ebf7dca5-1ff2-4c7e-8cc0-90fe23ce2459","eClass":"sysml:OwningMembership","data":{"elementId":"050b20dd-c60b-4c66-a750-936adb1472aa","ownedRelatedElement":[{"id":"98a777a3-15df-400f-a8d6-b5033e37e082","eClass":"sysml:Package","data":{"declaredName":"Package1","elementId":"4d6df3a5-cf34-4d2e-bdd4-e2792c4af562","ownedRelationship":[{"id":"0d7adc39-0682-4207-8048-687ec1fb3763","eClass":"sysml:OwningMembership","data":{"elementId":"ae6725c4-3b5f-4749-9aa6-8e828bc0b258","ownedRelatedElement":[{"id":"f42f0e49-8f00-49b4-b1ce-f4b0a98d3eb8","eClass":"sysml:AttributeDefinition","data":{"declaredName":"AttributeDefinition1","elementId":"c9b4cf7b-ebfd-492b-8809-ceff4df3efdb"}}]}}]}}]}}]}}]}}]}}]}', '2025-03-20 06:43:04.735581+00', '2025-03-20 06:43:04.735581+00');


--
-- Data for Name: image; Type: TABLE DATA; Schema: public; Owner: dbuser
--



--
-- Data for Name: library; Type: TABLE DATA; Schema: public; Owner: dbuser
--



--
-- Data for Name: project; Type: TABLE DATA; Schema: public; Owner: dbuser
--

INSERT INTO public.project (id, name, created_on, last_modified_on) VALUES ('df605b26-43ec-41b4-b30d-a4466d2df609', 'GeneralView-AddExistingElements', '2024-07-05 10:58:27.125531+00', '2024-07-05 10:58:44.172272+00');


--
-- Data for Name: nature; Type: TABLE DATA; Schema: public; Owner: dbuser
--



--
-- Data for Name: project_image; Type: TABLE DATA; Schema: public; Owner: dbuser
--



--
-- Data for Name: project_semantic_data; Type: TABLE DATA; Schema: public; Owner: dbuser
--

INSERT INTO public.project_semantic_data (id, project_id, semantic_data_id, name, created_on, last_modified_on) VALUES ('7896e428-3f22-4867-b2b4-b3b79873aeb3', 'df605b26-43ec-41b4-b30d-a4466d2df609', 'cbb4b3b4-af8e-4a05-a419-19dbfd1cc94d', 'main', '2024-07-05 10:58:27.155597+00', '2024-08-27 13:11:49.832903+00');


--
-- Data for Name: representation_metadata; Type: TABLE DATA; Schema: public; Owner: dbuser
--

INSERT INTO public.representation_metadata (id, target_object_id, description_id, label, kind, created_on, last_modified_on, documentation, semantic_data_id) VALUES ('110fea70-b1c2-4740-82da-584030618c80', '8d4123ac-3ac5-412d-90f2-49282b923003', 'siriusComponents://representationDescription?kind=diagramDescription&sourceKind=view&sourceId=8dcd14b0-6259-3193-ad2c-743f394c68e4&sourceElementId=db495705-e917-319b-af55-a32ad63f4089', 'General View', 'siriusComponents://representation?type=Diagram', '2024-01-01 09:42:00+00', '2024-01-02 09:42:00+00', '', 'cbb4b3b4-af8e-4a05-a419-19dbfd1cc94d');


--
-- Data for Name: representation_content; Type: TABLE DATA; Schema: public; Owner: dbuser
--

INSERT INTO public.representation_content (id, content, last_migration_performed, migration_version, created_on, last_modified_on) VALUES ('110fea70-b1c2-4740-82da-584030618c80', '{"id":"110fea70-b1c2-4740-82da-584030618c80","kind":"siriusComponents://representation?type=Diagram","targetObjectId":"8d4123ac-3ac5-412d-90f2-49282b923003","descriptionId":"siriusComponents://representationDescription?kind=diagramDescription&sourceKind=view&sourceId=8dcd14b0-6259-3193-ad2c-743f394c68e4&sourceElementId=db495705-e917-319b-af55-a32ad63f4089","nodes":[{"id":"76240424-f79c-3b90-b517-7eca5ab6fa8a","type":"node:image","targetObjectId":"8d4123ac-3ac5-412d-90f2-49282b923003","targetObjectKind":"siriusComponents://semantic?domain=sysml&entity=Package","targetObjectLabel":"Package 1","descriptionId":"siriusComponents://nodeDescription?sourceKind=view&sourceId=8dcd14b0-6259-3193-ad2c-743f394c68e4&sourceElementId=024ab54f-ab81-3d02-8abd-f65af641e6c6","borderNode":false,"modifiers":[],"state":"Normal","collapsingState":"EXPANDED","insideLabel":{"id":"2bfe9f44-e07e-3e96-bd7e-55aecbc1d982","text":"","insideLabelLocation":"TOP_CENTER","style":{"color":"#000000","fontSize":14,"bold":false,"italic":false,"underline":false,"strikeThrough":false,"iconURL":[],"background":"transparent","borderColor":"black","borderSize":0,"borderRadius":3,"borderStyle":"Solid","maxWidth":null},"isHeader":false,"headerSeparatorDisplayMode":"NEVER","overflowStrategy":"NONE","textAlign":"LEFT"},"outsideLabels":[],"style":{"imageURL":"images/add_your_first_element.svg","scalingFactor":1,"borderColor":"transparent","borderSize":1,"borderRadius":0,"borderStyle":"Solid","positionDependentRotation":false},"childrenLayoutStrategy":null,"borderNodes":[],"childNodes":[],"defaultWidth":1061,"defaultHeight":476,"labelEditable":false,"pinned":false}],"edges":[],"layoutData":{"nodeLayoutData":{"76240424-f79c-3b90-b517-7eca5ab6fa8a":{"id":"76240424-f79c-3b90-b517-7eca5ab6fa8a","position":{"x":0.0,"y":0.0},"size":{"width":1061.0,"height":476.0},"resizedByUser":false}},"edgeLayoutData":{},"labelLayoutData":{}}}', 'none', '2024.5.4-202407040900', '2024-01-01 09:42:00+00', '2025-02-17 16:01:41.079028+00');


--
-- Data for Name: semantic_data_dependency; Type: TABLE DATA; Schema: public; Owner: dbuser
--



--
-- Data for Name: semantic_data_domain; Type: TABLE DATA; Schema: public; Owner: dbuser
--

INSERT INTO public.semantic_data_domain (semantic_data_id, uri) VALUES ('cbb4b3b4-af8e-4a05-a419-19dbfd1cc94d', 'http://www.eclipse.org/syson/sysml');


--
-- PostgreSQL database dump complete
--

