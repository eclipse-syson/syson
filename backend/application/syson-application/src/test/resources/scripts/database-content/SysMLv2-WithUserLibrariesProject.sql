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

INSERT INTO public.semantic_data (id, created_on, last_modified_on) VALUES ('a1926aa3-a380-4e9f-82e4-fdd067444c2e', '2025-03-31 16:07:17.891503+00', '2025-03-31 16:07:47.726903+00');


--
-- Data for Name: document; Type: TABLE DATA; Schema: public; Owner: dbuser
--

INSERT INTO public.document (id, semantic_data_id, name, content, is_read_only, created_on, last_modified_on) VALUES ('f539bf95-61ec-4303-87c8-869c58ae5e4a', 'a1926aa3-a380-4e9f-82e4-fdd067444c2e', 'RW-user-library.sysml', '{"json":{"version":"1.0","encoding":"utf-8"},"ns":{"sysml":"http://www.eclipse.org/syson/sysml"},"migration":{"lastMigrationPerformed":"EdgeDescriptionSourceTargetDescriptionsParticipant","migrationVersion":"2025.4.0-202503171117"},"content":[{"id":"4bf86cd1-3eb9-409a-868f-83d942d5471f","eClass":"sysml:Namespace","data":{"eAnnotations":[{"source":"org.eclipse.syson.sysml.imported"}],"elementId":"42c49ef5-2051-4c32-88bd-986fa7382846","ownedRelationship":[{"id":"7c85e8b3-faf0-4e42-a1ee-e89ff9cae150","eClass":"sysml:OwningMembership","data":{"elementId":"d89377e4-2d77-42be-99bc-b1d56585b390","ownedRelatedElement":[{"id":"584b6543-6afb-4978-bbe1-0f3d52a4e443","eClass":"sysml:LibraryPackage","data":{"declaredName":"Package2","elementId":"ced96349-3868-491f-b8bc-fdaf0c31246d","ownedRelationship":[{"id":"2a1a0008-a3af-435c-b0f1-7a878d0d70d6","eClass":"sysml:OwningMembership","data":{"elementId":"eae2ac34-8a76-4c11-9a68-c671c97e4b5c","ownedRelatedElement":[{"id":"9ddb9f7b-364b-4142-9ae7-3be9a6156113","eClass":"sysml:PartDefinition","data":{"declaredName":"PartDefX","elementId":"64e8ffa5-8a0b-4645-9a66-159b646f2de3"}}]}}]}}]}}]}}]}', false, '2025-03-31 16:07:47.720021+00', '2025-03-31 16:07:47.720021+00');
INSERT INTO public.document (id, semantic_data_id, name, content, is_read_only, created_on, last_modified_on) VALUES ('020c0f16-2c40-44a9-a009-a05957845346', 'a1926aa3-a380-4e9f-82e4-fdd067444c2e', 'RO-user-library.sysml', '{"json":{"version":"1.0","encoding":"utf-8"},"ns":{"sysml":"http://www.eclipse.org/syson/sysml"},"migration":{"lastMigrationPerformed":"EdgeDescriptionSourceTargetDescriptionsParticipant","migrationVersion":"2025.4.0-202503171117"},"content":[{"id":"f32af328-8fe0-41ed-a8a6-a6d8f36c6daf","eClass":"sysml:Namespace","data":{"eAnnotations":[{"source":"org.eclipse.syson.sysml.imported"}],"elementId":"37e3a123-4931-4991-86a0-9cf821925ac8","ownedRelationship":[{"id":"d70a35f6-e9e6-40b0-8f49-acd61dbbfd31","eClass":"sysml:OwningMembership","data":{"elementId":"baa237ab-bdc6-4abf-8544-c518e2309744","ownedRelatedElement":[{"id":"ff891075-cbba-4bac-9f22-4ea5f49d0cf0","eClass":"sysml:LibraryPackage","data":{"declaredName":"Package3","elementId":"0f4d4a6a-36ec-41d1-b066-a0c7f8a79afd","ownedRelationship":[{"id":"f4257323-ee79-4e82-aafe-160e006771fd","eClass":"sysml:OwningMembership","data":{"elementId":"c7ec616b-c7cc-4a7e-b5f8-bf6ad8c76f32","ownedRelatedElement":[{"id":"caad8eb5-4cfd-4f12-b383-48f1b1810dab","eClass":"sysml:PartDefinition","data":{"declaredName":"PartDefX","elementId":"ada41840-43c8-4b30-b76d-49670a2c2c00"}}]}}]}}]}}]}}]}', true, '2025-03-31 16:07:47.720021+00', '2025-03-31 16:07:47.720021+00');

--
-- Data for Name: image; Type: TABLE DATA; Schema: public; Owner: dbuser
--



--
-- Data for Name: library; Type: TABLE DATA; Schema: public; Owner: dbuser
--



--
-- Data for Name: project; Type: TABLE DATA; Schema: public; Owner: dbuser
--

INSERT INTO public.project (id, name, created_on, last_modified_on) VALUES ('f8d096cd-ce00-4552-b11a-47b7859d8918', 'SysMLv2-WithUserLibrariesProject', '2025-03-31 16:07:17.717908+00', '2025-03-31 16:07:30.909144+00');


--
-- Data for Name: nature; Type: TABLE DATA; Schema: public; Owner: dbuser
--



--
-- Data for Name: project_image; Type: TABLE DATA; Schema: public; Owner: dbuser
--



--
-- Data for Name: project_semantic_data; Type: TABLE DATA; Schema: public; Owner: dbuser
--

INSERT INTO public.project_semantic_data (id, project_id, semantic_data_id, name, created_on, last_modified_on) VALUES ('d9ed348d-7440-435b-8416-c33fd9680ec3', 'f8d096cd-ce00-4552-b11a-47b7859d8918', 'a1926aa3-a380-4e9f-82e4-fdd067444c2e', 'main', '2025-03-31 16:07:17.915322+00', '2025-03-31 16:07:17.915322+00');


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

INSERT INTO public.semantic_data_domain (semantic_data_id, uri) VALUES ('a1926aa3-a380-4e9f-82e4-fdd067444c2e', 'http://www.eclipse.org/syson/sysml');


--
-- PostgreSQL database dump complete
--

