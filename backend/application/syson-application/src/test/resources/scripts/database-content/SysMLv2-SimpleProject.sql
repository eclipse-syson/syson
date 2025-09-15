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

INSERT INTO public.semantic_data (id, created_on, last_modified_on) VALUES ('4b5adc0c-90a2-48c6-9ae7-c3fc035ff38b', '2024-11-05 13:55:21.094331+00', '2024-11-05 13:57:15.80357+00');


--
-- Data for Name: document; Type: TABLE DATA; Schema: public; Owner: dbuser
--

INSERT INTO public.document (id, semantic_data_id, name, content, is_read_only, created_on, last_modified_on) VALUES ('9a59f836-1df2-4e5d-803c-9eb0ba7031aa', '4b5adc0c-90a2-48c6-9ae7-c3fc035ff38b', 'SysMLv2.sysml', '{"json":{"version":"1.0","encoding":"utf-8"},"ns":{"sysml":"http://www.eclipse.org/syson/sysml"},"content":[{"id":"686dee81-b94c-4393-b15b-65dd73dfdd24","eClass":"sysml:Namespace","data":{"elementId":"bea38691-2169-4b70-87ac-ffa289824fde","ownedRelationship":[{"id":"e3c74bf2-55ba-4b0d-a4f5-9ab326f372c4","eClass":"sysml:OwningMembership","data":{"elementId":"a6fce401-4aaf-47c8-88a6-d75462e3c6db","ownedRelatedElement":[{"id":"127c38e7-0e15-4232-aa02-76b342e3324a","eClass":"sysml:Package","data":{"declaredName":"Package1","elementId":"d51791b8-6666-46e3-8c60-c975e1f3e490","ownedRelationship":[{"id":"0642ab55-a317-40c1-aa52-cc16306d937b","eClass":"sysml:NamespaceImport","data":{"elementId":"9f902e4a-4ca9-4787-92d8-713b3b06c490","importedNamespace":"40c37406-f22d-4424-981f-99935f039244"}},{"id":"883deb19-59a8-4c58-8887-978bf11fffcd","eClass":"sysml:OwningMembership","data":{"elementId":"be359545-8e91-439c-8bdf-0638ab702f9d","ownedRelatedElement":[{"id":"a4f51a38-bfeb-4e0d-a870-55f8fe90405e","eClass":"sysml:PartUsage","data":{"declaredName":"p","elementId":"d99634bb-d4e0-4afa-bbe4-ee553005937d","ownedRelationship":[{"id":"1ba5c6e8-1c6b-44f6-8a05-f82f2c7a2538","eClass":"sysml:FeatureTyping","data":{"elementId":"9bfede6e-a370-451e-aa33-c1102942dcb7","type":"47acdb65-63f9-4bd5-8675-e2596241dc2c","typedFeature":"d99634bb-d4e0-4afa-bbe4-ee553005937d"}}],"isComposite":true}}]}}]}}]}},{"id":"13001db6-0353-48ba-8bec-157bbd9a3b57","eClass":"sysml:OwningMembership","data":{"elementId":"cd1165da-f4a1-4962-90f6-d117e8c7a354","ownedRelatedElement":[{"id":"ec12f223-8639-42a3-96c2-34163c6eccce","eClass":"sysml:Package","data":{"declaredName":"Package2","elementId":"40c37406-f22d-4424-981f-99935f039244","ownedRelationship":[{"id":"bbe9c050-d108-4807-8c62-7d20c31817f6","eClass":"sysml:OwningMembership","data":{"elementId":"c0c5cec5-6606-4145-98c7-eef80964e95a","ownedRelatedElement":[{"id":"0a70220d-707e-4a88-84dc-6aa43aa97269","eClass":"sysml:PartDefinition","data":{"declaredName":"PartDefX","elementId":"47acdb65-63f9-4bd5-8675-e2596241dc2c"}}]}}]}}]}}]}}]}', false, '2024-11-05 13:57:15.803558+00', '2024-11-05 13:57:15.803558+00');


--
-- Data for Name: image; Type: TABLE DATA; Schema: public; Owner: dbuser
--



--
-- Data for Name: library; Type: TABLE DATA; Schema: public; Owner: dbuser
--



--
-- Data for Name: project; Type: TABLE DATA; Schema: public; Owner: dbuser
--

INSERT INTO public.project (id, name, created_on, last_modified_on) VALUES ('a427f187-9003-498c-9178-72e8350cc67c', 'SysMLv2-SimpleProject', '2024-11-05 13:55:20.956951+00', '2024-11-05 13:55:38.969023+00');


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

