--
-- PostgreSQL database dump
--

-- Dumped from database version 17.5 (Debian 17.5-1.pgdg120+1)
-- Dumped by pg_dump version 17.6

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

INSERT INTO public.semantic_data (id, created_on, last_modified_on) VALUES ('4b5adc0c-90a2-48c6-9ae7-c3fc035ff38b', '2024-11-05 13:55:21.094331+00', '2025-09-25 12:24:39.579767+00');


--
-- Data for Name: document; Type: TABLE DATA; Schema: public; Owner: dbuser
--

INSERT INTO public.document (id, semantic_data_id, name, content, created_on, last_modified_on, is_read_only) VALUES ('9a59f836-1df2-4e5d-803c-9eb0ba7031aa', '4b5adc0c-90a2-48c6-9ae7-c3fc035ff38b', 'SysMLv2.sysml', '{"json":{"version":"1.0","encoding":"utf-8"},"ns":{"sysml":"http://www.eclipse.org/syson/sysml"},"migration":{"lastMigrationPerformed":"OneDiagramDescriptionMigrationParticipant$$SpringCGLIB$$0","migrationVersion":"2025.8.0-202508220000"},"content":[{"id":"686dee81-b94c-4393-b15b-65dd73dfdd24","eClass":"sysml:Namespace","data":{"elementId":"bea38691-2169-4b70-87ac-ffa289824fde","ownedRelationship":[{"id":"e3c74bf2-55ba-4b0d-a4f5-9ab326f372c4","eClass":"sysml:OwningMembership","data":{"elementId":"a6fce401-4aaf-47c8-88a6-d75462e3c6db","ownedRelatedElement":[{"id":"127c38e7-0e15-4232-aa02-76b342e3324a","eClass":"sysml:Package","data":{"declaredName":"Package1","elementId":"d51791b8-6666-46e3-8c60-c975e1f3e490","ownedRelationship":[{"id":"0642ab55-a317-40c1-aa52-cc16306d937b","eClass":"sysml:NamespaceImport","data":{"elementId":"9f902e4a-4ca9-4787-92d8-713b3b06c490","importedNamespace":"ec12f223-8639-42a3-96c2-34163c6eccce"}},{"id":"883deb19-59a8-4c58-8887-978bf11fffcd","eClass":"sysml:OwningMembership","data":{"elementId":"be359545-8e91-439c-8bdf-0638ab702f9d","ownedRelatedElement":[{"id":"a4f51a38-bfeb-4e0d-a870-55f8fe90405e","eClass":"sysml:PartUsage","data":{"declaredName":"p","elementId":"d99634bb-d4e0-4afa-bbe4-ee553005937d","ownedRelationship":[{"id":"1ba5c6e8-1c6b-44f6-8a05-f82f2c7a2538","eClass":"sysml:FeatureTyping","data":{"elementId":"9bfede6e-a370-451e-aa33-c1102942dcb7","type":"0a70220d-707e-4a88-84dc-6aa43aa97269","typedFeature":"a4f51a38-bfeb-4e0d-a870-55f8fe90405e"}},{"id":"d0af8b7c-1a69-4817-be08-13fd7379fd81","eClass":"sysml:FeatureMembership","data":{"elementId":"9700175b-699c-4201-bfbd-8ce3d90cbc5f","ownedRelatedElement":[{"id":"c3b15305-e5fb-44a9-96a1-4e0245fc4f2c","eClass":"sysml:ViewUsage","data":{"declaredName":"view4","elementId":"bdedde81-86db-4f74-887b-20af07833c25","ownedRelationship":[{"id":"395ef21e-36d7-4d9a-9586-99a631fcedf0","eClass":"sysml:FeatureTyping","data":{"elementId":"edb72ff4-e724-4387-9dd6-7b3e7095607e","type":"sysml:ViewDefinition sysmllibrary:///faf517ae-dbcd-30a4-b3b9-3d9cb3bbf5c1#6518462a-2f51-5276-b95e-69ee5193db38","typedFeature":"c3b15305-e5fb-44a9-96a1-4e0245fc4f2c"}}]}}]}}],"isComposite":true}}]}},{"id":"a5e72c79-5496-4770-a4c7-296a56f47617","eClass":"sysml:OwningMembership","data":{"elementId":"4e21e33f-7920-49fe-8889-b4fab257dc78","ownedRelatedElement":[{"id":"17df78b1-ad06-4861-827e-c1cf15eed2a5","eClass":"sysml:ViewUsage","data":{"declaredName":"view1","elementId":"0a0c297f-a58b-4ef8-a4f6-8d0ff197b238","ownedRelationship":[{"id":"60d64858-90b3-44ed-af19-b966d72a8a8b","eClass":"sysml:FeatureTyping","data":{"elementId":"da3ad6ee-3e58-4ed4-a41a-88f7b3d1dbfa","type":"sysml:ViewDefinition sysmllibrary:///faf517ae-dbcd-30a4-b3b9-3d9cb3bbf5c1#03904fdf-d6f2-57b1-92d5-95d36b8208dc","typedFeature":"17df78b1-ad06-4861-827e-c1cf15eed2a5"}}],"isComposite":true}}]}},{"id":"28feab55-0a2d-4d69-85d5-29fe3b069b71","eClass":"sysml:OwningMembership","data":{"elementId":"41ccfd33-8013-4684-82b5-f279decaad1b","ownedRelatedElement":[{"id":"c7a3fc13-d643-469c-a0ad-5939869a2bee","eClass":"sysml:ViewUsage","data":{"declaredName":"view2","elementId":"dd76563a-251d-4c3e-a4bf-108304672629","ownedRelationship":[{"id":"ed5facc4-54a7-41f9-b4ec-6f08fc4061b5","eClass":"sysml:FeatureTyping","data":{"elementId":"c2d81d09-5098-4775-bb74-b746a70fe510","type":"sysml:ViewDefinition sysmllibrary:///faf517ae-dbcd-30a4-b3b9-3d9cb3bbf5c1#e1e8729f-a571-520c-b3f8-1b8dcdf5014d","typedFeature":"c7a3fc13-d643-469c-a0ad-5939869a2bee"}}]}}]}},{"id":"505066be-cc52-4edb-a1df-17a29242e4be","eClass":"sysml:OwningMembership","data":{"elementId":"c20baa6f-ffc6-4178-8dc1-e074b9e68782","ownedRelatedElement":[{"id":"cca0616d-49d3-4311-a390-2511576cf759","eClass":"sysml:ViewUsage","data":{"declaredName":"view3","elementId":"c691780a-a8a9-4444-acba-d33cfd628b73","ownedRelationship":[{"id":"82cc5a01-8b63-4e60-a7b1-df7631d00e22","eClass":"sysml:FeatureTyping","data":{"elementId":"5fa23d7d-50a7-407a-a631-f800ad514d8c","type":"sysml:ViewDefinition sysmllibrary:///faf517ae-dbcd-30a4-b3b9-3d9cb3bbf5c1#073ba87b-4f0a-5a2e-8ce5-71f9dc645098","typedFeature":"cca0616d-49d3-4311-a390-2511576cf759"}}]}}]}}]}}]}},{"id":"13001db6-0353-48ba-8bec-157bbd9a3b57","eClass":"sysml:OwningMembership","data":{"elementId":"cd1165da-f4a1-4962-90f6-d117e8c7a354","ownedRelatedElement":[{"id":"ec12f223-8639-42a3-96c2-34163c6eccce","eClass":"sysml:Package","data":{"declaredName":"Package2","elementId":"40c37406-f22d-4424-981f-99935f039244","ownedRelationship":[{"id":"bbe9c050-d108-4807-8c62-7d20c31817f6","eClass":"sysml:OwningMembership","data":{"elementId":"c0c5cec5-6606-4145-98c7-eef80964e95a","ownedRelatedElement":[{"id":"0a70220d-707e-4a88-84dc-6aa43aa97269","eClass":"sysml:PartDefinition","data":{"declaredName":"PartDefX","elementId":"47acdb65-63f9-4bd5-8675-e2596241dc2c"}}]}}]}}]}}]}}]}', '2025-09-25 12:24:39.579761+00', '2025-09-25 12:24:39.579761+00', false);


--
-- Data for Name: image; Type: TABLE DATA; Schema: public; Owner: dbuser
--



--
-- Data for Name: library; Type: TABLE DATA; Schema: public; Owner: dbuser
--



--
-- Data for Name: project; Type: TABLE DATA; Schema: public; Owner: dbuser
--

INSERT INTO public.project (id, name, created_on, last_modified_on) VALUES ('a427f187-9003-498c-9178-72e8350cc67c', 'ExplorerView-DirectEdit', '2024-11-05 13:55:20.956951+00', '2025-09-25 12:28:42.525205+00');


--
-- Data for Name: nature; Type: TABLE DATA; Schema: public; Owner: dbuser
--



--
-- Data for Name: project_image; Type: TABLE DATA; Schema: public; Owner: dbuser
--



--
-- Data for Name: project_semantic_data; Type: TABLE DATA; Schema: public; Owner: dbuser
--

INSERT INTO public.project_semantic_data (id, project_id, semantic_data_id, name, created_on, last_modified_on) VALUES ('1c18ce38-862f-46a1-ad5d-0433cff8a7ab', 'a427f187-9003-498c-9178-72e8350cc67c', '4b5adc0c-90a2-48c6-9ae7-c3fc035ff38b', 'main', '2024-11-05 13:55:21.094331+00', '2024-11-05 13:57:15.80357+00');


--
-- Data for Name: representation_metadata; Type: TABLE DATA; Schema: public; Owner: dbuser
--



--
-- Data for Name: representation_content; Type: TABLE DATA; Schema: public; Owner: dbuser
--



--
-- Data for Name: semantic_data_dependency; Type: TABLE DATA; Schema: public; Owner: dbuser
--



--
-- Data for Name: semantic_data_domain; Type: TABLE DATA; Schema: public; Owner: dbuser
--

INSERT INTO public.semantic_data_domain (semantic_data_id, uri) VALUES ('4b5adc0c-90a2-48c6-9ae7-c3fc035ff38b', 'http://www.eclipse.org/syson/sysml');


--
-- PostgreSQL database dump complete
--

