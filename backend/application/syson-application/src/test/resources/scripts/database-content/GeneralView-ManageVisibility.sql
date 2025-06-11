--
-- PostgreSQL database dump
--

-- Dumped from database version 16.3
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

INSERT INTO public.semantic_data (id, created_on, last_modified_on) VALUES ('b01d9a82-7f04-46f9-9b49-d69b291ae105', '2025-06-04 17:03:27.448954+02', '2025-06-11 09:35:18.172449+02');


--
-- Data for Name: document; Type: TABLE DATA; Schema: public; Owner: dbuser
--

INSERT INTO public.document (id, semantic_data_id, name, content, created_on, last_modified_on) VALUES ('63d7cf97-d21d-4f4a-8e8a-5b861bb3001f', 'b01d9a82-7f04-46f9-9b49-d69b291ae105', 'SysMLv2.sysml', '{"json":{"version":"1.0","encoding":"utf-8"},"ns":{"sysml":"http://www.eclipse.org/syson/sysml"},"migration":{"lastMigrationPerformed":"ViewUsageExposeElementMigrationParticipant","migrationVersion":"2025.6.0-202506110000"},"content":[{"id":"fc0825f3-2af2-4ece-8407-0c6081d36d71","eClass":"sysml:Namespace","data":{"elementId":"ea74c213-7753-412a-b43f-9e649e4e11a1","ownedRelationship":[{"id":"154c5930-402a-4b7a-ae0e-b3a7e340e456","eClass":"sysml:OwningMembership","data":{"elementId":"1b163dd0-96be-4777-a67f-646d1323caf6","ownedRelatedElement":[{"id":"8aac7c62-b505-4f47-88d8-cbf833c530ca","eClass":"sysml:Package","data":{"declaredName":"Package 1","elementId":"12da3492-af26-4554-9591-07171b11514e","ownedRelationship":[{"id":"f3f5784a-1d67-48e5-97d0-4faeecc36ac3","eClass":"sysml:OwningMembership","data":{"elementId":"891548d5-7aa0-45e0-9606-a7a8354ba35e","ownedRelatedElement":[{"id":"bc4a3f6e-ec9b-4925-986f-4318a039687b","eClass":"sysml:ViewUsage","data":{"declaredName":"General View","elementId":"115ab79e-53ca-4f88-b72c-9374d2023a3e","ownedRelationship":[{"id":"959edcf3-20bd-4340-813c-8f08c2129918","eClass":"sysml:FeatureTyping","data":{"elementId":"fc797b8e-429d-4dcc-b708-2e2cbecedeb1","type":"sysml:ViewDefinition sysmllibrary:///faf517ae-dbcd-30a4-b3b9-3d9cb3bbf5c1#7098b764-d891-48f5-b928-4ba41cbe837e","typedFeature":"bc4a3f6e-ec9b-4925-986f-4318a039687b"}}]}}]}},{"id":"7ab2fd1d-1c6b-4587-b8e9-b4424867e47e","eClass":"sysml:OwningMembership","data":{"elementId":"163f1df6-c801-42ff-83da-2bd1712c8536","ownedRelatedElement":[{"id":"de5ba8fe-7e99-4dd4-bc7b-a0b11f2ced3e","eClass":"sysml:ActionDefinition","data":{"declaredName":"ActionDefinition1","elementId":"b9ff6f3d-23fe-4b3d-8b00-8f8cbc6dc2ea","ownedRelationship":[{"id":"87359e12-cf65-4efb-b75e-afac1b7db76f","eClass":"sysml:OwningMembership","data":{"elementId":"f482d774-be75-474a-95a1-97a66d33af69","ownedRelatedElement":[{"id":"4ce08018-af91-4e1d-9d0e-dbb12339c382","eClass":"sysml:Documentation","data":{"elementId":"efd14641-42ed-40a5-ba2c-678a2d4afb74","body":"add doc here"}}]}}]}}]}}]}}]}}]}}]}', '2025-06-11 09:35:18.172449+02', '2025-06-11 09:35:18.172449+02');


--
-- Data for Name: image; Type: TABLE DATA; Schema: public; Owner: dbuser
--



--
-- Data for Name: library; Type: TABLE DATA; Schema: public; Owner: dbuser
--



--
-- Data for Name: project; Type: TABLE DATA; Schema: public; Owner: dbuser
--

INSERT INTO public.project (id, name, created_on, last_modified_on) VALUES ('bae3a3bd-409e-430b-b753-edf2ccc9ebd6', 'SysMLv2', '2025-06-04 17:03:27.436495+02', '2025-06-04 17:03:27.436495+02');


--
-- Data for Name: nature; Type: TABLE DATA; Schema: public; Owner: dbuser
--



--
-- Data for Name: project_image; Type: TABLE DATA; Schema: public; Owner: dbuser
--



--
-- Data for Name: project_semantic_data; Type: TABLE DATA; Schema: public; Owner: dbuser
--

INSERT INTO public.project_semantic_data (id, project_id, semantic_data_id, name, created_on, last_modified_on) VALUES ('43cc8e46-f570-4bcd-9cf4-0b3511d071d7', 'bae3a3bd-409e-430b-b753-edf2ccc9ebd6', 'b01d9a82-7f04-46f9-9b49-d69b291ae105', 'main', '2025-06-04 17:03:27.465469+02', '2025-06-04 17:03:27.465469+02');


--
-- Data for Name: representation_metadata; Type: TABLE DATA; Schema: public; Owner: dbuser
--

INSERT INTO public.representation_metadata (id, target_object_id, description_id, label, kind, created_on, last_modified_on, documentation, semantic_data_id) VALUES ('621aded7-5bd5-4966-9526-7ff0816dbb63', 'bc4a3f6e-ec9b-4925-986f-4318a039687b', 'siriusComponents://representationDescription?kind=diagramDescription&sourceKind=view&sourceId=8dcd14b0-6259-3193-ad2c-743f394c68e4&sourceElementId=db495705-e917-319b-af55-a32ad63f4089', 'General View', 'siriusComponents://representation?type=Diagram', '2025-06-04 17:03:28.714564+02', '2025-06-04 17:03:28.714564+02', '', 'b01d9a82-7f04-46f9-9b49-d69b291ae105');


--
-- Data for Name: representation_content; Type: TABLE DATA; Schema: public; Owner: dbuser
--

INSERT INTO public.representation_content (id, content, last_migration_performed, migration_version, created_on, last_modified_on) VALUES ('621aded7-5bd5-4966-9526-7ff0816dbb63', '{"id":"621aded7-5bd5-4966-9526-7ff0816dbb63","kind":"siriusComponents://representation?type=Diagram","targetObjectId":"bc4a3f6e-ec9b-4925-986f-4318a039687b","descriptionId":"siriusComponents://representationDescription?kind=diagramDescription&sourceKind=view&sourceId=8dcd14b0-6259-3193-ad2c-743f394c68e4&sourceElementId=db495705-e917-319b-af55-a32ad63f4089","nodes":[{"id":"9dd0a24d-0038-3149-9953-90811f2c5025","type":"node:rectangle","targetObjectId":"de5ba8fe-7e99-4dd4-bc7b-a0b11f2ced3e","targetObjectKind":"siriusComponents://semantic?domain=sysml&entity=ActionDefinition","targetObjectLabel":"ActionDefinition1","descriptionId":"siriusComponents://nodeDescription?sourceKind=view&sourceId=8dcd14b0-6259-3193-ad2c-743f394c68e4&sourceElementId=535cfc4c-559a-35e1-896e-fbfb4c5ddf1d","borderNode":false,"modifiers":[],"state":"Normal","collapsingState":"EXPANDED","insideLabel":{"id":"3f22718a-ce02-3522-b093-cbe2935f8b71","text":"Â«action defÂ»\nActionDefinition1","insideLabelLocation":"TOP_CENTER","style":{"color":"#000000","fontSize":14,"bold":false,"italic":false,"underline":false,"strikeThrough":false,"iconURL":["/icons/full/obj16/ActionDefinition.svg"],"background":"transparent","borderColor":"black","borderSize":0,"borderRadius":3,"borderStyle":"Solid","maxWidth":null},"isHeader":true,"headerSeparatorDisplayMode":"IF_CHILDREN","overflowStrategy":"WRAP","textAlign":"CENTER","customizedStyleProperties":[]},"outsideLabels":[],"style":{"background":"#ffffff","borderColor":"#000000","borderSize":1,"borderRadius":0,"borderStyle":"Solid","childrenLayoutStrategy":{"areChildNodesDraggable":false,"topGap":0,"bottomGap":0,"growableNodeIds":["siriusComponents://nodeDescription?sourceKind=view&sourceId=8dcd14b0-6259-3193-ad2c-743f394c68e4&sourceElementId=8c141ab4-6df3-3bf6-a8df-b837e821cde1"],"kind":"List"}},"borderNodes":[],"childNodes":[{"id":"ca7020a8-ba91-3708-9ca5-d79c9c1aaff8","type":"node:rectangle","targetObjectId":"de5ba8fe-7e99-4dd4-bc7b-a0b11f2ced3e","targetObjectKind":"siriusComponents://semantic?domain=sysml&entity=ActionDefinition","targetObjectLabel":"ActionDefinition1","descriptionId":"siriusComponents://nodeDescription?sourceKind=view&sourceId=8dcd14b0-6259-3193-ad2c-743f394c68e4&sourceElementId=9b111500-5e74-3c17-a829-9f8d7bb4242d","borderNode":false,"modifiers":["Hidden"],"state":"Hidden","collapsingState":"EXPANDED","insideLabel":{"id":"3f031074-afcf-3214-8596-d302067a44d8","text":"doc","insideLabelLocation":"TOP_CENTER","style":{"color":"#000000","fontSize":12,"bold":false,"italic":true,"underline":false,"strikeThrough":false,"iconURL":[],"background":"transparent","borderColor":"black","borderSize":0,"borderRadius":3,"borderStyle":"Solid","maxWidth":null},"isHeader":true,"headerSeparatorDisplayMode":"NEVER","overflowStrategy":"NONE","textAlign":"CENTER","customizedStyleProperties":[]},"outsideLabels":[],"style":{"background":"transparent","borderColor":"#000000","borderSize":1,"borderRadius":0,"borderStyle":"Solid","childrenLayoutStrategy":{"areChildNodesDraggable":true,"topGap":0,"bottomGap":10,"growableNodeIds":[],"kind":"List"}},"borderNodes":[],"childNodes":[{"id":"8886913b-3abb-3d83-9c54-2117537d5f52","type":"node:icon-label","targetObjectId":"4ce08018-af91-4e1d-9d0e-dbb12339c382","targetObjectKind":"siriusComponents://semantic?domain=sysml&entity=Documentation","targetObjectLabel":"","descriptionId":"siriusComponents://nodeDescription?sourceKind=view&sourceId=8dcd14b0-6259-3193-ad2c-743f394c68e4&sourceElementId=29224b0f-cc17-34b6-9cc1-ed306b6f4c49","borderNode":false,"modifiers":[],"state":"Hidden","collapsingState":"EXPANDED","insideLabel":{"id":"7d552d02-3453-3be6-a866-f354e742bdb0","text":"add doc here","insideLabelLocation":"TOP_LEFT","style":{"color":"#000000","fontSize":14,"bold":false,"italic":false,"underline":false,"strikeThrough":false,"iconURL":["/icons/full/obj16/Documentation.svg"],"background":"transparent","borderColor":"black","borderSize":0,"borderRadius":3,"borderStyle":"Solid","maxWidth":null},"isHeader":false,"headerSeparatorDisplayMode":"NEVER","overflowStrategy":"WRAP","textAlign":"LEFT","customizedStyleProperties":[]},"outsideLabels":[],"style":{"background":"transparent","childrenLayoutStrategy":{"kind":"FreeForm"}},"borderNodes":[],"childNodes":[],"defaultWidth":155,"defaultHeight":15,"labelEditable":true,"pinned":false,"customizedStyleProperties":[]}],"defaultWidth":155,"defaultHeight":60,"labelEditable":false,"pinned":false,"customizedStyleProperties":[]},{"id":"70e68465-92fb-3b33-9b69-e95316bc6279","type":"node:rectangle","targetObjectId":"de5ba8fe-7e99-4dd4-bc7b-a0b11f2ced3e","targetObjectKind":"siriusComponents://semantic?domain=sysml&entity=ActionDefinition","targetObjectLabel":"ActionDefinition1","descriptionId":"siriusComponents://nodeDescription?sourceKind=view&sourceId=8dcd14b0-6259-3193-ad2c-743f394c68e4&sourceElementId=10cfa9c8-0ab3-3005-9134-bc8a17a790ff","borderNode":false,"modifiers":["Hidden"],"state":"Hidden","collapsingState":"EXPANDED","insideLabel":{"id":"404bc774-5346-3a20-9413-e8486fd9ac10","text":"parameters","insideLabelLocation":"TOP_CENTER","style":{"color":"#000000","fontSize":12,"bold":false,"italic":true,"underline":false,"strikeThrough":false,"iconURL":[],"background":"transparent","borderColor":"black","borderSize":0,"borderRadius":3,"borderStyle":"Solid","maxWidth":null},"isHeader":true,"headerSeparatorDisplayMode":"NEVER","overflowStrategy":"NONE","textAlign":"CENTER","customizedStyleProperties":[]},"outsideLabels":[],"style":{"background":"transparent","borderColor":"#000000","borderSize":1,"borderRadius":0,"borderStyle":"Solid","childrenLayoutStrategy":{"areChildNodesDraggable":true,"topGap":0,"bottomGap":10,"growableNodeIds":[],"kind":"List"}},"borderNodes":[],"childNodes":[],"defaultWidth":155,"defaultHeight":60,"labelEditable":false,"pinned":false,"customizedStyleProperties":[]},{"id":"752c658b-1a42-3087-a479-8542cdb54915","type":"node:rectangle","targetObjectId":"de5ba8fe-7e99-4dd4-bc7b-a0b11f2ced3e","targetObjectKind":"siriusComponents://semantic?domain=sysml&entity=ActionDefinition","targetObjectLabel":"ActionDefinition1","descriptionId":"siriusComponents://nodeDescription?sourceKind=view&sourceId=8dcd14b0-6259-3193-ad2c-743f394c68e4&sourceElementId=41cc2762-86b7-3ac4-9b63-d8c3012bdb05","borderNode":false,"modifiers":["Hidden"],"state":"Hidden","collapsingState":"EXPANDED","insideLabel":{"id":"900b4f0a-3d9c-3f62-ab6a-7f50df6ccf6e","text":"actions","insideLabelLocation":"TOP_CENTER","style":{"color":"#000000","fontSize":12,"bold":false,"italic":true,"underline":false,"strikeThrough":false,"iconURL":[],"background":"transparent","borderColor":"black","borderSize":0,"borderRadius":3,"borderStyle":"Solid","maxWidth":null},"isHeader":true,"headerSeparatorDisplayMode":"NEVER","overflowStrategy":"NONE","textAlign":"CENTER","customizedStyleProperties":[]},"outsideLabels":[],"style":{"background":"transparent","borderColor":"#000000","borderSize":1,"borderRadius":0,"borderStyle":"Solid","childrenLayoutStrategy":{"areChildNodesDraggable":true,"topGap":0,"bottomGap":10,"growableNodeIds":[],"kind":"List"}},"borderNodes":[],"childNodes":[],"defaultWidth":155,"defaultHeight":60,"labelEditable":false,"pinned":false,"customizedStyleProperties":[]},{"id":"a2ec8a5e-b4b9-37bf-a33b-81600e1a8c53","type":"node:rectangle","targetObjectId":"de5ba8fe-7e99-4dd4-bc7b-a0b11f2ced3e","targetObjectKind":"siriusComponents://semantic?domain=sysml&entity=ActionDefinition","targetObjectLabel":"ActionDefinition1","descriptionId":"siriusComponents://nodeDescription?sourceKind=view&sourceId=8dcd14b0-6259-3193-ad2c-743f394c68e4&sourceElementId=8c141ab4-6df3-3bf6-a8df-b837e821cde1","borderNode":false,"modifiers":["Hidden","Faded"],"state":"Hidden","collapsingState":"EXPANDED","insideLabel":{"id":"f5699c7d-3975-3723-a629-f1f41dea72e9","text":"action flow","insideLabelLocation":"TOP_CENTER","style":{"color":"#000000","fontSize":12,"bold":false,"italic":true,"underline":false,"strikeThrough":false,"iconURL":[],"background":"transparent","borderColor":"black","borderSize":0,"borderRadius":3,"borderStyle":"Solid","maxWidth":null},"isHeader":true,"headerSeparatorDisplayMode":"NEVER","overflowStrategy":"NONE","textAlign":"CENTER","customizedStyleProperties":[]},"outsideLabels":[],"style":{"background":"transparent","borderColor":"#000000","borderSize":1,"borderRadius":0,"borderStyle":"Solid","childrenLayoutStrategy":{"kind":"FreeForm"}},"borderNodes":[],"childNodes":[{"id":"527bfabe-b369-3063-873b-413034a92b88","type":"node:image","targetObjectId":"03262155-734a-473f-9535-262925cd5b8b","targetObjectKind":"siriusComponents://semantic?domain=sysml&entity=ActionUsage","targetObjectLabel":"start","descriptionId":"siriusComponents://nodeDescription?sourceKind=view&sourceId=8dcd14b0-6259-3193-ad2c-743f394c68e4&sourceElementId=596f7e8d-ba35-38d6-937f-011e05b74063","borderNode":false,"modifiers":[],"state":"Hidden","collapsingState":"EXPANDED","insideLabel":null,"outsideLabels":[],"style":{"imageURL":"images/start_action.svg","scalingFactor":1,"borderColor":"transparent","borderSize":1,"borderRadius":0,"borderStyle":"Solid","positionDependentRotation":false,"childrenLayoutStrategy":{"kind":"FreeForm"}},"borderNodes":[],"childNodes":[],"defaultWidth":28,"defaultHeight":28,"labelEditable":false,"pinned":false,"customizedStyleProperties":[]}],"defaultWidth":155,"defaultHeight":150,"labelEditable":false,"pinned":false,"customizedStyleProperties":[]},{"id":"c0a1fd91-c473-3fcf-9233-3d28e9d2da09","type":"node:rectangle","targetObjectId":"de5ba8fe-7e99-4dd4-bc7b-a0b11f2ced3e","targetObjectKind":"siriusComponents://semantic?domain=sysml&entity=ActionDefinition","targetObjectLabel":"ActionDefinition1","descriptionId":"siriusComponents://nodeDescription?sourceKind=view&sourceId=8dcd14b0-6259-3193-ad2c-743f394c68e4&sourceElementId=5f8660e5-575b-3887-b9ce-30ba1fef43db","borderNode":false,"modifiers":["Hidden"],"state":"Hidden","collapsingState":"EXPANDED","insideLabel":{"id":"ff1d5791-368b-3e2c-b060-4972c1f867f6","text":"perform actions","insideLabelLocation":"TOP_CENTER","style":{"color":"#000000","fontSize":12,"bold":false,"italic":true,"underline":false,"strikeThrough":false,"iconURL":[],"background":"transparent","borderColor":"black","borderSize":0,"borderRadius":3,"borderStyle":"Solid","maxWidth":null},"isHeader":true,"headerSeparatorDisplayMode":"NEVER","overflowStrategy":"NONE","textAlign":"CENTER","customizedStyleProperties":[]},"outsideLabels":[],"style":{"background":"transparent","borderColor":"#000000","borderSize":1,"borderRadius":0,"borderStyle":"Solid","childrenLayoutStrategy":{"areChildNodesDraggable":true,"topGap":0,"bottomGap":10,"growableNodeIds":[],"kind":"List"}},"borderNodes":[],"childNodes":[],"defaultWidth":155,"defaultHeight":60,"labelEditable":false,"pinned":false,"customizedStyleProperties":[]}],"defaultWidth":155,"defaultHeight":60,"labelEditable":true,"pinned":false,"customizedStyleProperties":[]}],"edges":[],"layoutData":{"nodeLayoutData":{"70e68465-92fb-3b33-9b69-e95316bc6279":{"id":"70e68465-92fb-3b33-9b69-e95316bc6279","position":{"x":1.0,"y":131.0},"size":{"width":155.0,"height":60.0},"resizedByUser":false,"handleLayoutData":[]},"9dd0a24d-0038-3149-9953-90811f2c5025":{"id":"9dd0a24d-0038-3149-9953-90811f2c5025","position":{"x":161.21322311479773,"y":-80.5052192753291},"size":{"width":155.0,"height":71.0},"resizedByUser":false,"handleLayoutData":[]},"ca7020a8-ba91-3708-9ca5-d79c9c1aaff8":{"id":"ca7020a8-ba91-3708-9ca5-d79c9c1aaff8","position":{"x":1.0,"y":71.0},"size":{"width":155.0,"height":60.0},"resizedByUser":false,"handleLayoutData":[]},"a2ec8a5e-b4b9-37bf-a33b-81600e1a8c53":{"id":"a2ec8a5e-b4b9-37bf-a33b-81600e1a8c53","position":{"x":1.0,"y":131.0},"size":{"width":155.0,"height":150.0},"resizedByUser":false,"handleLayoutData":[]},"527bfabe-b369-3063-873b-413034a92b88":{"id":"527bfabe-b369-3063-873b-413034a92b88","position":{"x":8.0,"y":33.0},"size":{"width":28.0,"height":28.0},"resizedByUser":false,"handleLayoutData":[]},"8886913b-3abb-3d83-9c54-2117537d5f52":{"id":"8886913b-3abb-3d83-9c54-2117537d5f52","position":{"x":0.0,"y":33.0},"size":{"width":155.0,"height":17.0},"resizedByUser":false,"handleLayoutData":[]},"752c658b-1a42-3087-a479-8542cdb54915":{"id":"752c658b-1a42-3087-a479-8542cdb54915","position":{"x":1.0,"y":191.0},"size":{"width":155.0,"height":60.0},"resizedByUser":false,"handleLayoutData":[]},"c0a1fd91-c473-3fcf-9233-3d28e9d2da09":{"id":"c0a1fd91-c473-3fcf-9233-3d28e9d2da09","position":{"x":1.0,"y":401.0},"size":{"width":155.0,"height":60.0},"resizedByUser":false,"handleLayoutData":[]}},"edgeLayoutData":{},"labelLayoutData":{}}}', 'none', '2025.6.0-202506011650', '2025-06-04 17:03:28.741677+02', '2025-06-11 14:40:52.358553+02');


--
-- Data for Name: semantic_data_dependency; Type: TABLE DATA; Schema: public; Owner: dbuser
--



--
-- Data for Name: semantic_data_domain; Type: TABLE DATA; Schema: public; Owner: dbuser
--

INSERT INTO public.semantic_data_domain (semantic_data_id, uri) VALUES ('b01d9a82-7f04-46f9-9b49-d69b291ae105', 'http://www.eclipse.org/syson/sysml');