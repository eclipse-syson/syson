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

INSERT INTO public.semantic_data (id, created_on, last_modified_on) VALUES ('62bc073d-9b5c-4aaa-9a1c-44e727fc1898', '2025-09-24 09:12:10.485886+00', '2025-09-24 09:13:26.542996+00');


--
-- Data for Name: document; Type: TABLE DATA; Schema: public; Owner: dbuser
--

INSERT INTO public.document (id, semantic_data_id, name, content, created_on, last_modified_on, is_read_only) VALUES ('59aaff8d-4cfb-4ee9-9cd6-09129dc37702', '62bc073d-9b5c-4aaa-9a1c-44e727fc1898', 'SysMLv2.sysml', '{"json":{"version":"1.0","encoding":"utf-8"},"ns":{"sysml":"http://www.eclipse.org/syson/sysml"},"migration":{"lastMigrationPerformed":"none","migrationVersion":"2025.8.0-202508220000"},"content":[{"id":"9f2d1708-219a-4136-af72-8382e965c9b6","eClass":"sysml:Namespace","data":{"elementId":"17c685e7-ddda-4f8b-ab43-b50073fa17d6","ownedRelationship":[{"id":"17fad87c-e730-47e8-9439-d138df0d6d5f","eClass":"sysml:OwningMembership","data":{"elementId":"48e99a1f-f50e-455f-88b8-5d6b7ccd22d6","ownedRelatedElement":[{"id":"2de8a9aa-929a-4efb-85d4-b188c4682a9a","eClass":"sysml:Package","data":{"declaredName":"Package1","elementId":"51ef7a57-034b-4061-9b46-b647fbeb6451","ownedRelationship":[{"id":"a25665aa-e45e-4128-9544-752664dfcc6f","eClass":"sysml:OwningMembership","data":{"elementId":"d9f29320-44f1-4d82-a068-ade4149914e8","ownedRelatedElement":[{"id":"fbf003a8-6ab9-4a90-be51-dea091f0783e","eClass":"sysml:ViewUsage","data":{"declaredName":"view1","elementId":"cf05a05e-62c0-4e28-9874-b2900b9096d7"}}]}},{"id":"c54a6af9-086c-4fb7-85d2-c8e2858bfb4a","eClass":"sysml:OwningMembership","data":{"elementId":"703c4cff-b257-4ec3-9e01-9e467b1e3454","ownedRelatedElement":[{"id":"36dbf043-a777-4053-8793-1029ccbe14dc","eClass":"sysml:RequirementUsage","data":{"declaredName":"requirement1","elementId":"996ec0ef-46ed-4d47-833c-622a07fce7c8","ownedRelationship":[{"id":"52704522-fa34-4e1f-ba7a-413830262d37","eClass":"sysml:OwningMembership","data":{"elementId":"2b4a01d9-9dde-4f87-80e7-a939473b71d6","ownedRelatedElement":[{"id":"8d31f3a3-7a38-4af7-a4fe-1f535cc69cb5","eClass":"sysml:Documentation","data":{"elementId":"e2a3cc32-fda6-44ba-8dc5-975c3ad3747f","body":"doc R1"}}]}}],"isComposite":true,"reqId":"ReqR1"}}]}},{"id":"696239fa-160f-41d7-b0c2-3b96bae2bd60","eClass":"sysml:OwningMembership","data":{"elementId":"644fa892-7ea2-4ad4-93bd-87b8f5fb26b4","ownedRelatedElement":[{"id":"43047917-0f14-4fb6-b3c5-c848273c6e14","eClass":"sysml:RequirementUsage","data":{"declaredName":"requirement2","elementId":"b22831ef-a859-40d5-a12a-89f6889fb819","ownedRelationship":[{"id":"4d249efc-5745-4272-a927-47d90015fa19","eClass":"sysml:OwningMembership","data":{"elementId":"75e32352-700a-4edc-ae63-21cf1ee4b021","ownedRelatedElement":[{"id":"099ee63d-7ef7-401e-997a-0ce0f33db7f4","eClass":"sysml:Documentation","data":{"elementId":"bc49f77c-d583-474d-9997-5938827673bb","body":"doc R2"}}]}}],"isComposite":true,"reqId":"ReqR2"}}]}}]}}]}}]}}]}', '2025-09-24 09:13:26.542964+00', '2025-09-24 09:13:26.542964+00', false);


--
-- Data for Name: image; Type: TABLE DATA; Schema: public; Owner: dbuser
--



--
-- Data for Name: library; Type: TABLE DATA; Schema: public; Owner: dbuser
--



--
-- Data for Name: project; Type: TABLE DATA; Schema: public; Owner: dbuser
--

INSERT INTO public.project (id, name, created_on, last_modified_on) VALUES ('0bbae5c2-5128-43e5-9aa1-697968663973', 'RequirementsTable', '2025-09-24 09:12:10.457854+00', '2025-09-24 09:12:20.472371+00');


--
-- Data for Name: nature; Type: TABLE DATA; Schema: public; Owner: dbuser
--



--
-- Data for Name: project_image; Type: TABLE DATA; Schema: public; Owner: dbuser
--



--
-- Data for Name: project_semantic_data; Type: TABLE DATA; Schema: public; Owner: dbuser
--

INSERT INTO public.project_semantic_data (id, project_id, semantic_data_id, name, created_on, last_modified_on) VALUES ('f1d62b1b-bc09-4a82-afcf-89701bceb4ba', '0bbae5c2-5128-43e5-9aa1-697968663973', '62bc073d-9b5c-4aaa-9a1c-44e727fc1898', 'main', '2025-09-24 09:12:10.49635+00', '2025-09-24 09:12:10.49635+00');


--
-- Data for Name: representation_metadata; Type: TABLE DATA; Schema: public; Owner: dbuser
--

INSERT INTO public.representation_metadata (id, representation_metadata_id, target_object_id, description_id, label, kind, created_on, last_modified_on, documentation, semantic_data_id) VALUES ('62bc073d-9b5c-4aaa-9a1c-44e727fc1898#0683aba1-3206-4ace-aac5-62120b01f344', '0683aba1-3206-4ace-aac5-62120b01f344', 'fbf003a8-6ab9-4a90-be51-dea091f0783e', 'siriusComponents://representationDescription?kind=tableDescription&sourceKind=view&sourceId=f445c867-006e-3b07-9385-40143a87f533&sourceElementId=05ffd328-a162-3d7e-b09b-7757ef23ef01', 'view1', 'siriusComponents://representation?type=Table', '2025-09-24 09:12:34.262372+00', '2025-09-24 09:12:34.262372+00', '', '62bc073d-9b5c-4aaa-9a1c-44e727fc1898');


--
-- Data for Name: representation_content; Type: TABLE DATA; Schema: public; Owner: dbuser
--

INSERT INTO public.representation_content (id, representation_metadata_id, semantic_data_id, content, last_migration_performed, migration_version, created_on, last_modified_on) VALUES ('62bc073d-9b5c-4aaa-9a1c-44e727fc1898#0683aba1-3206-4ace-aac5-62120b01f344', '0683aba1-3206-4ace-aac5-62120b01f344', '62bc073d-9b5c-4aaa-9a1c-44e727fc1898', '{"id":"0683aba1-3206-4ace-aac5-62120b01f344","kind":"siriusComponents://representation?type=Table","targetObjectId":"fbf003a8-6ab9-4a90-be51-dea091f0783e","targetObjectKind":"siriusComponents://semantic?domain=sysml&entity=ViewUsage","descriptionId":"siriusComponents://representationDescription?kind=tableDescription&sourceKind=view&sourceId=f445c867-006e-3b07-9385-40143a87f533&sourceElementId=05ffd328-a162-3d7e-b09b-7757ef23ef01","stripeRow":true,"lines":[],"columns":[{"id":"4fe41d55-8608-34d1-b6d3-64e15611fe50","targetObjectId":"DeclaredName","targetObjectKind":"","descriptionId":"siriusComponents://columnDescription?sourceKind=view&sourceId=f445c867-006e-3b07-9385-40143a87f533&sourceElementId=51d7f6df-a502-33bd-b270-b54895183960","headerLabel":"DeclaredName","headerIconURLs":[],"headerIndexLabel":"","width":250,"resizable":true,"hidden":false,"filterVariant":"","sortable":true,"index":0},{"id":"5c9213de-2247-3508-8d82-785a11db7741","targetObjectId":"ReqId","targetObjectKind":"","descriptionId":"siriusComponents://columnDescription?sourceKind=view&sourceId=f445c867-006e-3b07-9385-40143a87f533&sourceElementId=1e54ee10-dc2f-3965-86d9-05d6c6011240","headerLabel":"ReqId","headerIconURLs":[],"headerIndexLabel":"","width":150,"resizable":true,"hidden":false,"filterVariant":"","sortable":true,"index":0},{"id":"380554d8-5a01-3b9c-9bea-238f9ed84e5d","targetObjectId":"Documentation","targetObjectKind":"","descriptionId":"siriusComponents://columnDescription?sourceKind=view&sourceId=f445c867-006e-3b07-9385-40143a87f533&sourceElementId=a627de2f-49a9-351e-9ebd-1fcca9743a04","headerLabel":"Documentation","headerIconURLs":[],"headerIndexLabel":"","width":400,"resizable":true,"hidden":false,"filterVariant":"","sortable":true,"index":0}],"paginationData":{"hasPreviousPage":false,"hasNextPage":false,"totalRowCount":0},"globalFilter":"","columnFilters":[],"enableSubRows":false,"columnSort":[],"pageSizeOptions":[10,20,50],"defaultPageSize":10}', 'none', '0', '2025-09-24 09:12:34.27862+00', '2025-09-24 09:12:34.27862+00');


--
-- Data for Name: semantic_data_dependency; Type: TABLE DATA; Schema: public; Owner: dbuser
--



--
-- Data for Name: semantic_data_domain; Type: TABLE DATA; Schema: public; Owner: dbuser
--

INSERT INTO public.semantic_data_domain (semantic_data_id, uri) VALUES ('62bc073d-9b5c-4aaa-9a1c-44e727fc1898', 'http://www.eclipse.org/syson/sysml');


--
-- PostgreSQL database dump complete
--

