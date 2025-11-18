-- My RA Friend Master Data
-- Insert master medications and rehabilitation exercises

USE my_ra_friend;

-- ============================================
-- MEDICATIONS MASTER DATA
-- ============================================

INSERT INTO medications (name, category, description, common_side_effects) VALUES
-- DMARDs
('Methotrexate', 'DMARD', 'First-line DMARD for RA treatment', 'Nausea, mouth sores, liver toxicity, fatigue'),
('Hydroxychloroquine', 'DMARD', 'Antimalarial drug used for mild RA', 'Eye problems, nausea, skin rash'),
('Sulfasalazine', 'DMARD', 'DMARD effective for early RA', 'Nausea, headache, rash, abdominal pain'),
('Leflunomide', 'DMARD', 'DMARD that reduces inflammation', 'Diarrhea, elevated liver enzymes, hair loss'),
('Azathioprine', 'DMARD', 'Immunosuppressive DMARD', 'Nausea, increased infection risk'),

-- Biologics
('Adalimumab', 'Biologic', 'TNF-alpha inhibitor biologic', 'Injection site reactions, increased infection risk'),
('Etanercept', 'Biologic', 'TNF-alpha inhibitor biologic', 'Injection site reactions, infections'),
('Infliximab', 'Biologic', 'TNF-alpha inhibitor given IV', 'Infusion reactions, infections'),
('Tocilizumab', 'Biologic', 'IL-6 receptor inhibitor', 'Increased cholesterol, liver enzyme elevation'),
('Rituximab', 'Biologic', 'B-cell depleting biologic', 'Infusion reactions, increased infection risk'),

-- Steroids
('Prednisone', 'Steroid', 'Corticosteroid for inflammation control', 'Weight gain, osteoporosis, high blood sugar, mood changes'),
('Prednisolone', 'Steroid', 'Corticosteroid for inflammation', 'Similar to prednisone'),
('Methylprednisolone', 'Steroid', 'Injectable corticosteroid', 'Similar to prednisone'),

-- Supplements
('Folic Acid', 'Supplement', 'Reduces methotrexate side effects', 'Generally well tolerated'),
('Calcium + Vitamin D', 'Supplement', 'Bone health support', 'Constipation (calcium)'),
('Omega-3 Fish Oil', 'Supplement', 'May reduce inflammation', 'Fishy aftertaste, GI upset'),

-- PPIs (Proton Pump Inhibitors)
('Omeprazole', 'PPI', 'Reduces stomach acid, protects from NSAID damage', 'Headache, diarrhea, vitamin B12 deficiency'),
('Pantoprazole', 'PPI', 'Stomach acid reducer', 'Similar to omeprazole'),
('Esomeprazole', 'PPI', 'Stomach acid reducer', 'Similar to omeprazole'),

-- Other medications
('Ibuprofen', 'Other', 'NSAID for pain relief', 'Stomach upset, ulcers, kidney issues'),
('Naproxen', 'Other', 'NSAID for pain and inflammation', 'Similar to ibuprofen'),
('Celecoxib', 'Other', 'COX-2 selective NSAID', 'Less GI side effects than traditional NSAIDs'),
('Acetaminophen', 'Other', 'Pain reliever (not anti-inflammatory)', 'Liver toxicity at high doses'),
('Tramadol', 'Other', 'Opioid pain medication', 'Dizziness, nausea, constipation, dependence risk');

-- ============================================
-- REHABILITATION EXERCISES MASTER DATA
-- ============================================

INSERT INTO rehab_exercises (name, description, target_area, difficulty_level, instructions) VALUES
('Quadriceps Isometric',
 'Strengthen quadriceps without knee movement',
 'Knee',
 'beginner',
 '1. Sit with leg straight\n2. Tighten thigh muscle\n3. Push back of knee down into surface\n4. Hold for 5-10 seconds\n5. Relax and repeat'),

('Finger Squeezing Exercise',
 'Improve grip strength and finger flexibility',
 'Hand',
 'beginner',
 '1. Hold a soft ball or putty\n2. Squeeze firmly\n3. Hold for 3-5 seconds\n4. Release slowly\n5. Repeat 10 times'),

('Wrist Flexor Isometrics',
 'Strengthen wrist flexor muscles',
 'Wrist',
 'beginner',
 '1. Place forearm on table, palm up\n2. Make a fist\n3. Press wrist down into table\n4. Hold for 5-10 seconds\n5. Relax and repeat'),

('Wrist Extensor Isometrics',
 'Strengthen wrist extensor muscles',
 'Wrist',
 'beginner',
 '1. Place forearm on table, palm down\n2. Press back of hand down into table\n3. Hold for 5-10 seconds\n4. Relax and repeat'),

('Shoulder Isometric External Rotation',
 'Strengthen rotator cuff muscles',
 'Shoulder',
 'intermediate',
 '1. Stand next to wall\n2. Bend elbow 90 degrees\n3. Press back of hand into wall\n4. Hold for 5-10 seconds\n5. Relax and repeat'),

('Shoulder Isometric Internal Rotation',
 'Strengthen rotator cuff muscles',
 'Shoulder',
 'intermediate',
 '1. Stand facing wall\n2. Bend elbow 90 degrees\n3. Press palm into wall\n4. Hold for 5-10 seconds\n5. Relax and repeat'),

('Neck Isometric Flexion',
 'Strengthen neck flexor muscles',
 'Neck',
 'beginner',
 '1. Sit upright\n2. Place hand on forehead\n3. Gently push head forward into hand\n4. Resist with hand\n5. Hold 5 seconds, relax'),

('Neck Isometric Extension',
 'Strengthen neck extensor muscles',
 'Neck',
 'beginner',
 '1. Sit upright\n2. Place hands on back of head\n3. Gently push head backward into hands\n4. Resist with hands\n5. Hold 5 seconds, relax'),

('Grip Strengthening with Putty',
 'Improve hand and finger strength',
 'Hand',
 'intermediate',
 '1. Hold therapy putty in palm\n2. Squeeze and knead\n3. Roll into ball\n4. Pinch with fingers\n5. Perform for 5 minutes'),

('Ankle Isometric Dorsiflexion',
 'Strengthen ankle dorsiflexor muscles',
 'Ankle',
 'beginner',
 '1. Sit with foot flat on floor\n2. Press ball of foot down\n3. Lift toes up (keep heel down)\n4. Hold for 5-10 seconds\n5. Relax and repeat'),

('Hip Isometric Abduction',
 'Strengthen hip abductor muscles',
 'Hip',
 'intermediate',
 '1. Lie on side\n2. Press top leg down into bottom leg\n3. Try to lift while resisting\n4. Hold for 5-10 seconds\n5. Relax and repeat'),

('Elbow Flexor Isometrics',
 'Strengthen biceps without movement',
 'Elbow',
 'beginner',
 '1. Sit with elbow bent 90 degrees\n2. Place other hand on wrist\n3. Try to bend elbow more\n4. Resist with other hand\n5. Hold 5 seconds, relax');

-- Master data inserted successfully
