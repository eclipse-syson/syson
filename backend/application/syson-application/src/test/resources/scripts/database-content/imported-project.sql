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

INSERT INTO public.semantic_data (id, created_on, last_modified_on) VALUES ('4246b4f9-665c-48fe-be8b-41f735a940c4', '2025-03-31 16:07:17.891503+00', '2025-03-31 16:07:47.726903+00');


--
-- Data for Name: document; Type: TABLE DATA; Schema: public; Owner: dbuser
--

INSERT INTO public.document (id, semantic_data_id, name, content, created_on, last_modified_on) VALUES ('e223e894-85e2-4651-b592-87c3506738bc', '4246b4f9-665c-48fe-be8b-41f735a940c4', 'SysMLv2.sysml', '{"json":{"version":"1.0","encoding":"utf-8"},"ns":{"sysml":"http://www.eclipse.org/syson/sysml"},"migration":{"lastMigrationPerformed":"EdgeDescriptionSourceTargetDescriptionsParticipant","migrationVersion":"2025.4.0-202503171117"},"content":[{"id":"4bf86cd1-3eb9-409a-868f-83d942d5471f","eClass":"sysml:Namespace","data":{"eAnnotations":[{"source":"org.eclipse.syson.sysml.imported"}],"elementId":"42c49ef5-2051-4c32-88bd-986fa7382846","ownedRelationship":[{"id":"6b2eb8ee-8895-49a6-be85-dadb9a34ca17","eClass":"sysml:OwningMembership","data":{"elementId":"7c903a90-13d5-4a33-8ad5-d8ab3a56d1ee","ownedRelatedElement":[{"id":"246be267-f05e-475a-bdba-b95f6e533a21","eClass":"sysml:Package","data":{"declaredName":"Package1","elementId":"8a7bd952-616b-4f0f-8328-d595281ac1fd","ownedRelationship":[{"id":"4315617b-bae9-47a8-a298-cc366c910a7e","eClass":"sysml:NamespaceImport","data":{"elementId":"51b8c861-bbfc-41df-9a84-623e4af8af71","importedNamespace":"ced96349-3868-491f-b8bc-fdaf0c31246d"}},{"id":"870364ce-099a-475b-8bb0-df449ab0f35e","eClass":"sysml:OwningMembership","data":{"elementId":"2e1b0e4d-94c8-4162-b77b-d7c5ce296745","ownedRelatedElement":[{"id":"e7d5aa61-4fb9-46e0-a60c-ef770ff5e5a9","eClass":"sysml:PartUsage","data":{"declaredName":"p","elementId":"a3b273e5-7834-40cc-9b5e-aed28c2c4201","ownedRelationship":[{"id":"d14d156c-6fe0-4382-a5d4-15bdf6f82bf9","eClass":"sysml:FeatureTyping","data":{"elementId":"0bfc305d-1ac3-4ff8-9cf0-a84ef6e18055","type":"64e8ffa5-8a0b-4645-9a66-159b646f2de3","typedFeature":"a3b273e5-7834-40cc-9b5e-aed28c2c4201"}}],"isComposite":true}}]}}]}}]}},{"id":"7c85e8b3-faf0-4e42-a1ee-e89ff9cae150","eClass":"sysml:OwningMembership","data":{"elementId":"d89377e4-2d77-42be-99bc-b1d56585b390","ownedRelatedElement":[{"id":"584b6543-6afb-4978-bbe1-0f3d52a4e443","eClass":"sysml:Package","data":{"declaredName":"Package2","elementId":"ced96349-3868-491f-b8bc-fdaf0c31246d","ownedRelationship":[{"id":"2a1a0008-a3af-435c-b0f1-7a878d0d70d6","eClass":"sysml:OwningMembership","data":{"elementId":"eae2ac34-8a76-4c11-9a68-c671c97e4b5c","ownedRelatedElement":[{"id":"9ddb9f7b-364b-4142-9ae7-3be9a6156113","eClass":"sysml:PartDefinition","data":{"declaredName":"PartDefX","elementId":"64e8ffa5-8a0b-4645-9a66-159b646f2de3"}}]}}]}}]}}]}}]}', '2025-03-31 16:07:47.720021+00', '2025-03-31 16:07:47.720021+00');


--
-- Data for Name: image; Type: TABLE DATA; Schema: public; Owner: dbuser
--



--
-- Data for Name: library; Type: TABLE DATA; Schema: public; Owner: dbuser
--



--
-- Data for Name: project; Type: TABLE DATA; Schema: public; Owner: dbuser
--

INSERT INTO public.project (id, name, created_on, last_modified_on) VALUES ('afffb8f5-3db6-4b47-b295-55a36984db2e', 'SysMLv2-ImportedProject', '2025-03-31 16:07:17.717908+00', '2025-03-31 16:07:30.909144+00');


--
-- Data for Name: nature; Type: TABLE DATA; Schema: public; Owner: dbuser
--



--
-- Data for Name: project_image; Type: TABLE DATA; Schema: public; Owner: dbuser
--



--
-- Data for Name: project_semantic_data; Type: TABLE DATA; Schema: public; Owner: dbuser
--

INSERT INTO public.project_semantic_data (id, project_id, semantic_data_id, name, created_on, last_modified_on) VALUES ('d9ed348d-7440-435b-8416-c33fd9680ec3', 'afffb8f5-3db6-4b47-b295-55a36984db2e', '4246b4f9-665c-48fe-be8b-41f735a940c4', 'main', '2025-03-31 16:07:17.915322+00', '2025-03-31 16:07:17.915322+00');


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

INSERT INTO public.semantic_data_domain (semantic_data_id, uri) VALUES ('4246b4f9-665c-48fe-be8b-41f735a940c4', 'http://www.eclipse.org/syson/sysml');


--
-- PostgreSQL database dump complete
--

