-- Reuse GeneralView-WithTopNodes, changing only the persisted collapsing state of its ordinary part.
UPDATE public.representation_content
SET content = jsonb_set(
    content::jsonb,
    ARRAY['nodes', (
        SELECT (ordinality - 1)::text
        FROM jsonb_array_elements(content::jsonb->'nodes') WITH ORDINALITY AS nodes(node, ordinality)
        WHERE node->>'id' = '4c4fe0d5-4974-377e-9113-9ab022c75f8c'
    ), 'collapsingState'],
    '"COLLAPSED"'::jsonb
)::text
WHERE id = '1b0c2b5c-5bbe-4429-a73b-43ef46c16955#fa8c8a8d-2813-404c-876f-c04e8b297134';
