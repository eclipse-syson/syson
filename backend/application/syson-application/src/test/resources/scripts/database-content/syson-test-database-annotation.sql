--
-- PostgreSQL database dump
--

-- Dumped from database version 12.19 (Debian 12.19-1.pgdg120+1)
-- Dumped by pg_dump version 17.2

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

INSERT INTO public.semantic_data (id, created_on, last_modified_on) VALUES ('45343945-1cb8-456b-b396-b02df4ca6da1', '2025-03-12 13:51:25.097775+00', '2025-03-12 13:53:06.701798+00');


--
-- Data for Name: document; Type: TABLE DATA; Schema: public; Owner: dbuser
--

INSERT INTO public.document (id, semantic_data_id, name, content, is_read_only, created_on, last_modified_on) VALUES ('12f4e92d-7bd7-4214-a10b-8000056b4ab0', '45343945-1cb8-456b-b396-b02df4ca6da1', 'Annotation-annotatingElement.sysml', '{"json":{"version":"1.0","encoding":"utf-8"},"ns":{"sysml":"http://www.eclipse.org/syson/sysml"},"migration":{"lastMigrationPerformed":"SuccessionAsUsageMigrationParticipant","migrationVersion":"2025.4.0-202502270000"},"content":[{"id":"97443fc5-83db-47c2-99c6-c9cf2eaa93bd","eClass":"sysml:Namespace","data":{"elementId":"af2c2677-c0d6-4c06-bec1-fe4b89924e2f","ownedRelationship":[{"id":"73352002-3cbb-4aef-89e3-b17d1e0c57f2","eClass":"sysml:OwningMembership","data":{"elementId":"f1455c3f-4a4b-410a-bc5c-0b31dfaffaee","ownedRelatedElement":[{"id":"f930ca78-0c16-4cf5-9726-88b6e859ba9b","eClass":"sysml:Package","data":{"declaredName":"Package 1","elementId":"6735756e-f2e9-494e-88af-e8cc822039a1","ownedRelationship":[{"id":"c160921f-995b-4e69-b8e3-356ea38be21f","eClass":"sysml:OwningMembership","data":{"elementId":"80c2671d-779c-44b5-af79-e63bedf05355","ownedRelatedElement":[{"id":"45820ab8-26c2-4cfe-b94b-9486cda326f4","eClass":"sysml:PartDefinition","data":{"declaredName":"PartDefinition1","elementId":"03a00751-c081-497c-aa6d-09c9f3ef265b","ownedRelationship":[{"id":"a745278f-cd02-43f0-a304-1d2bd945c366","eClass":"sysml:Annotation","data":{"elementId":"13567c66-16d5-4a86-92d5-191af9d2a84a","ownedRelatedElement":[{"id":"75ca7884-4748-40b2-bc09-5aa35a40a6c4","eClass":"sysml:Documentation","data":{"elementId":"150bf3b9-bd26-404d-98ae-f2487b3104f3","annotation":["13567c66-16d5-4a86-92d5-191af9d2a84a"],"body":"doc1"}}],"annotatingElement":"150bf3b9-bd26-404d-98ae-f2487b3104f3"}}]}}]}}]}}]}}]}}]}', false, '2025-03-12 13:53:06.701783+00', '2025-03-12 13:53:06.701783+00');


--
-- Data for Name: image; Type: TABLE DATA; Schema: public; Owner: dbuser
--



--
-- Data for Name: project; Type: TABLE DATA; Schema: public; Owner: dbuser
--

INSERT INTO public.project (id, name, created_on, last_modified_on) VALUES ('9aa96183-3abe-4998-ae3a-fde4ec3fe915', 'Annotation-annotatingElement MigrationParticipant - Test', '2025-03-12 13:51:25.047144+00', '2025-03-12 13:51:58.07614+00');


--
-- Data for Name: nature; Type: TABLE DATA; Schema: public; Owner: dbuser
--



--
-- Data for Name: project_image; Type: TABLE DATA; Schema: public; Owner: dbuser
--



--
-- Data for Name: project_semantic_data; Type: TABLE DATA; Schema: public; Owner: dbuser
--

INSERT INTO public.project_semantic_data (id, project_id, semantic_data_id, name, created_on, last_modified_on) VALUES ('1f3ba8a3-c89c-403e-8f8d-7b13f56a0706', '9aa96183-3abe-4998-ae3a-fde4ec3fe915', '45343945-1cb8-456b-b396-b02df4ca6da1', 'main', '2025-03-12 13:51:25.117222+00', '2025-03-12 13:51:25.117222+00');


--
-- Data for Name: representation_metadata; Type: TABLE DATA; Schema: public; Owner: dbuser
--



--
-- Data for Name: representation_content; Type: TABLE DATA; Schema: public; Owner: dbuser
--



--
-- Data for Name: semantic_data_domain; Type: TABLE DATA; Schema: public; Owner: dbuser
--

INSERT INTO public.semantic_data_domain (semantic_data_id, uri) VALUES ('45343945-1cb8-456b-b396-b02df4ca6da1', 'http://www.eclipse.org/syson/sysml');


--
-- PostgreSQL database dump complete
--

