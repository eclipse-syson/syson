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

INSERT INTO public.semantic_data (id, created_on, last_modified_on) VALUES ('0d49f9d5-1895-42c6-961f-614b2bc5d9d1', '2025-11-05 10:54:16.390315+00', '2025-11-05 10:54:16.390315+00');
INSERT INTO public.semantic_data (id, created_on, last_modified_on) VALUES ('9c17ca3b-c1fe-43e2-92a4-c14c052a6d1a', '2025-11-05 10:54:21.968354+00', '2025-11-05 10:54:45.608478+00');


--
-- Data for Name: document; Type: TABLE DATA; Schema: public; Owner: dbuser
--

INSERT INTO public.document (id, semantic_data_id, name, content, created_on, last_modified_on, is_read_only) VALUES ('2d4fbc26-ca0f-4031-97c0-2eb589a3d6b2', '0d49f9d5-1895-42c6-961f-614b2bc5d9d1', 'Library.sysml', '{"json":{"version":"1.0","encoding":"utf-8"},"ns":{"sysml":"http://www.eclipse.org/syson/sysml"},"content":[{"id":"0c85e4a5-11a8-4a69-9300-769939b4cea9","eClass":"sysml:Namespace","data":{"elementId":"8eb1782d-b613-4dec-86a8-17e9d8de23b6","ownedRelationship":[{"id":"db782b47-7042-4a08-bf12-0626f109f2f9","eClass":"sysml:OwningMembership","data":{"elementId":"01721c31-d2b0-47da-9be0-3f28dfbd0084","ownedRelatedElement":[{"id":"cf16727c-34ce-4a67-8a39-72a6f355099c","eClass":"sysml:LibraryPackage","data":{"declaredName":"LibraryPackage","elementId":"80862083-afbd-499b-ac8f-ea3f8bcb5bf3","ownedRelationship":[{"id":"0ecbccef-86d6-4136-be84-be6c6cd36b1e","eClass":"sysml:OwningMembership","data":{"elementId":"779c5c2c-9ebc-427d-838d-c2776a00e4d9","ownedRelatedElement":[{"id":"2981a675-2b7f-469e-b70e-5397b41e1b08","eClass":"sysml:PartUsage","data":{"declaredName":"part1","elementId":"492e88d7-151a-4de0-a4b6-c7307ba8f5c5","isComposite":true}}]}}]}}]}}]}}]}', '2025-11-05 10:54:16.387971+00', '2025-11-05 10:54:16.387971+00', false);
INSERT INTO public.document (id, semantic_data_id, name, content, created_on, last_modified_on, is_read_only) VALUES ('b9b540b8-5e5a-4be6-af25-0dc485063706', '9c17ca3b-c1fe-43e2-92a4-c14c052a6d1a', 'SysMLv2.sysml', '{"json":{"version":"1.0","encoding":"utf-8"},"ns":{"sysml":"http://www.eclipse.org/syson/sysml"},"migration":{"lastMigrationPerformed":"none","migrationVersion":"2025.8.0-202508220000"},"content":[{"id":"232fa7d5-426a-4e58-84c1-39e938cf7899","eClass":"sysml:Namespace","data":{"elementId":"7e70b201-44e6-4a48-8eeb-d93d62a3cab9","ownedRelationship":[{"id":"853543a8-b275-40d6-a9f2-16127fb5a2f2","eClass":"sysml:OwningMembership","data":{"elementId":"42b0e5ab-2fe2-4deb-a0b9-a375d67e540c","ownedRelatedElement":[{"id":"111a9370-400e-4dce-b99a-2d7d2d59ce2e","eClass":"sysml:Package","data":{"declaredName":"Package1","elementId":"a7a902b8-6997-4e24-bdee-620d16c1fb05","ownedRelationship":[{"id":"58708c70-feb5-4762-acde-ec2b4a72d725","eClass":"sysml:OwningMembership","data":{"elementId":"3618ceda-64d2-4895-b073-39d3f3edd920","ownedRelatedElement":[{"id":"c0a50dd1-c057-439d-9cd2-506d9a685f5b","eClass":"sysml:ViewUsage","data":{"declaredName":"view1","elementId":"418ef77f-6178-4e48-bfa7-bab88e82bed5","ownedRelationship":[{"id":"026d6a3d-22d6-4f19-aead-f17ea775b89d","eClass":"sysml:FeatureTyping","data":{"elementId":"16ed79e0-7d50-469b-8481-5d3e994c3143","type":"sysml:ViewDefinition sysmllibrary:///faf517ae-dbcd-30a4-b3b9-3d9cb3bbf5c1#03904fdf-d6f2-57b1-92d5-95d36b8208dc","typedFeature":"c0a50dd1-c057-439d-9cd2-506d9a685f5b"}}]}}]}}]}}]}}]}}]}', '2025-11-05 10:54:24.290828+00', '2025-11-05 10:54:24.290828+00', false);


--
-- Data for Name: image; Type: TABLE DATA; Schema: public; Owner: dbuser
--



--
-- Data for Name: library; Type: TABLE DATA; Schema: public; Owner: dbuser
--

INSERT INTO public.library (id, namespace, name, version, semantic_data_id, description, created_on, last_modified_on) VALUES ('c8deceb5-3ffe-4145-a866-4a4dbccefb9a', '465216c6-63f0-4d19-b10c-8923ddb829db', 'LibraryWithOneLibraryPackage', '1.0.0', '0d49f9d5-1895-42c6-961f-614b2bc5d9d1', '', '2025-11-05 10:54:16.432936+00', '2025-11-05 10:54:16.432936+00');


--
-- Data for Name: project; Type: TABLE DATA; Schema: public; Owner: dbuser
--

INSERT INTO public.project (id, name, created_on, last_modified_on) VALUES ('607aeefc-defc-414c-ad1c-c355b801fa59', 'SysMLv2', '2025-11-05 10:54:21.932491+00', '2025-11-05 10:54:21.932491+00');


--
-- Data for Name: nature; Type: TABLE DATA; Schema: public; Owner: dbuser
--



--
-- Data for Name: project_image; Type: TABLE DATA; Schema: public; Owner: dbuser
--



--
-- Data for Name: project_semantic_data; Type: TABLE DATA; Schema: public; Owner: dbuser
--

INSERT INTO public.project_semantic_data (id, project_id, semantic_data_id, name, created_on, last_modified_on) VALUES ('c9c2d6e9-f2e7-453e-b282-c71155823e5a', '607aeefc-defc-414c-ad1c-c355b801fa59', '9c17ca3b-c1fe-43e2-92a4-c14c052a6d1a', 'main', '2025-11-05 10:54:21.995865+00', '2025-11-05 10:54:21.995865+00');


--
-- Data for Name: representation_metadata; Type: TABLE DATA; Schema: public; Owner: dbuser
--

INSERT INTO public.representation_metadata (id, representation_metadata_id, target_object_id, description_id, label, kind, created_on, last_modified_on, documentation, semantic_data_id) VALUES ('9c17ca3b-c1fe-43e2-92a4-c14c052a6d1a#e083314c-dfb3-4f79-8205-e3b27c54cbd3', 'e083314c-dfb3-4f79-8205-e3b27c54cbd3', 'c0a50dd1-c057-439d-9cd2-506d9a685f5b', 'siriusComponents://representationDescription?kind=diagramDescription&sourceKind=view&sourceId=8dcd14b0-6259-3193-ad2c-743f394c68e4&sourceElementId=db495705-e917-319b-af55-a32ad63f4089', 'view1', 'siriusComponents://representation?type=Diagram', '2025-11-05 10:54:24.240922+00', '2025-11-05 10:54:24.240922+00', '', '9c17ca3b-c1fe-43e2-92a4-c14c052a6d1a');


--
-- Data for Name: representation_content; Type: TABLE DATA; Schema: public; Owner: dbuser
--

INSERT INTO public.representation_content (id, representation_metadata_id, semantic_data_id, content, last_migration_performed, migration_version, created_on, last_modified_on) VALUES ('9c17ca3b-c1fe-43e2-92a4-c14c052a6d1a#e083314c-dfb3-4f79-8205-e3b27c54cbd3', 'e083314c-dfb3-4f79-8205-e3b27c54cbd3', '9c17ca3b-c1fe-43e2-92a4-c14c052a6d1a', '{"id":"e083314c-dfb3-4f79-8205-e3b27c54cbd3","kind":"siriusComponents://representation?type=Diagram","targetObjectId":"c0a50dd1-c057-439d-9cd2-506d9a685f5b","descriptionId":"siriusComponents://representationDescription?kind=diagramDescription&sourceKind=view&sourceId=8dcd14b0-6259-3193-ad2c-743f394c68e4&sourceElementId=db495705-e917-319b-af55-a32ad63f4089","nodes":[{"id":"af926758-6aea-3342-a9bf-1494585ef65d","type":"node:image","targetObjectId":"c0a50dd1-c057-439d-9cd2-506d9a685f5b","targetObjectKind":"siriusComponents://semantic?domain=sysml&entity=ViewUsage","targetObjectLabel":"view1","descriptionId":"siriusComponents://nodeDescription?sourceKind=view&sourceId=8dcd14b0-6259-3193-ad2c-743f394c68e4&sourceElementId=024ab54f-ab81-3d02-8abd-f65af641e6c6","borderNode":false,"initialBorderNodePosition":"NONE","modifiers":[],"state":"Normal","collapsingState":"EXPANDED","insideLabel":{"id":"4b01361a-4c47-33de-89da-86d788e480df","text":"","insideLabelLocation":"TOP_CENTER","style":{"color":"#000000","fontSize":14,"bold":false,"italic":false,"underline":false,"strikeThrough":false,"iconURL":[],"background":"transparent","borderColor":"black","borderSize":0,"borderRadius":3,"borderStyle":"Solid","maxWidth":null,"visibility":"visible"},"isHeader":false,"headerSeparatorDisplayMode":"NEVER","overflowStrategy":"NONE","textAlign":"LEFT","customizedStyleProperties":[]},"outsideLabels":[],"style":{"imageURL":"images/add_your_first_element.svg","scalingFactor":1,"borderColor":"transparent","borderSize":1,"borderRadius":0,"borderStyle":"Solid","positionDependentRotation":false,"childrenLayoutStrategy":{"kind":"FreeForm"}},"borderNodes":[],"childNodes":[],"defaultWidth":1061,"defaultHeight":476,"labelEditable":false,"pinned":false,"customizedStyleProperties":[]}],"edges":[],"layoutData":{"nodeLayoutData":{"af926758-6aea-3342-a9bf-1494585ef65d":{"id":"af926758-6aea-3342-a9bf-1494585ef65d","position":{"x":0.0,"y":0.0},"size":{"width":1061.0,"height":476.0},"resizedByUser":false,"handleLayoutData":[]}},"edgeLayoutData":{},"labelLayoutData":{}}}', 'none', '2025.8.0-202506301700', '2025-11-05 10:54:24.266943+00', '2025-11-05 10:54:27.461649+00');


--
-- Data for Name: semantic_data_dependency; Type: TABLE DATA; Schema: public; Owner: dbuser
--

INSERT INTO public.semantic_data_dependency (semantic_data_id, dependency_semantic_data_id, index) VALUES ('9c17ca3b-c1fe-43e2-92a4-c14c052a6d1a', '0d49f9d5-1895-42c6-961f-614b2bc5d9d1', 0);


--
-- Data for Name: semantic_data_domain; Type: TABLE DATA; Schema: public; Owner: dbuser
--

INSERT INTO public.semantic_data_domain (semantic_data_id, uri) VALUES ('0d49f9d5-1895-42c6-961f-614b2bc5d9d1', 'http://www.eclipse.org/syson/sysml');
INSERT INTO public.semantic_data_domain (semantic_data_id, uri) VALUES ('9c17ca3b-c1fe-43e2-92a4-c14c052a6d1a', 'http://www.eclipse.org/syson/sysml');


--
-- PostgreSQL database dump complete
--

