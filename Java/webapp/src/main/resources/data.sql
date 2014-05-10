insert into municipalities (version, name, active) values (1, 'Pargas', true);
insert into municipalities (version, name, active) values (1, 'Kimitoön', true);

insert into assignment_types (version, code, description, active) values (1, '103', 'Brandlarm', true);
insert into assignment_types (version, code, description, active) values (1, '202', 'Trafikolycka: liten', true);
insert into assignment_types (version, code, description, active) values (1, '203', 'Trafikolycka: medelstor', true);
insert into assignment_types (version, code, description, active) values (1, '401', 'Byggnadsbrand: liten', true);
insert into assignment_types (version, code, description, active) values (1, '402', 'Byggnadsbrand: medelstor', true);
insert into assignment_types (version, code, description, active) values (1, '411', 'Fordonsbrand: liten', true);
insert into assignment_types (version, code, description, active) values (1, '412', 'Fordonsbrand: medelstor', true);
insert into assignment_types (version, code, description, active) values (1, '421', 'Terrängbrand: liten', true);
insert into assignment_types (version, code, description, active) values (1, '422', 'Terrängbrand: medelstor', true);
insert into assignment_types (version, code, description, active) values (1, '431', 'Annan brand: liten', true);
insert into assignment_types (version, code, description, active) values (1, '432', 'Annan brand: medelstor', true);

insert into resource_types (id, version, code, description, active) values (1, 1, 'RP2', 'Jourhavande chef', true);
insert into resource_types (id, version, code, description, active) values (2, 1, 'RP3', 'Jourhavande befäl', true);
insert into resource_types (id, version, code, description, active) values (3, 1, 'R1', 'Släckningsbil', true);
insert into resource_types (id, version, code, description, active) values (4, 1, 'R2', 'Tank-släckningsbil', true);
insert into resource_types (id, version, code, description, active) values (5, 1, 'R3', 'Tankbil', true);
insert into resource_types (id, version, code, description, active) values (6, 1, 'R7', 'Manskapsbil', true);
insert into resource_types (id, version, code, description, active) values (7, 1, 'E0', 'Läkarenhet', true);
insert into resource_types (id, version, code, description, active) values (8, 1, 'E1', 'Akutvårdens fältchef', true);
insert into resource_types (id, version, code, description, active) values (9, 1, 'E2', 'Ambulans vårdnivå', true);
insert into resource_types (id, version, code, description, active) values (10, 1, 'E3', 'Ambulans grundnivå', true);

insert into resources (id, version, type_id, call_sign, active) values (1, 1, 1, 'RAP20', true);
insert into resources (id, version, type_id, call_sign, active) values (2, 1, 2, 'RAP30', true);
insert into resources (id, version, type_id, call_sign, active) values (3, 1, 3, 'RAP101', true);
insert into resources (id, version, type_id, call_sign, active) values (4, 1, 3, 'RAP201', true);
insert into resources (id, version, type_id, call_sign, active) values (5, 1, 4, 'RAP302', true);
insert into resources (id, version, type_id, call_sign, active) values (6, 1, 5, 'RAP103', true);
insert into resources (id, version, type_id, call_sign, active) values (7, 1, 6, 'RAP207', true);
insert into resources (id, version, type_id, call_sign, active) values (8, 1, 7, 'EAP00', true);
insert into resources (id, version, type_id, call_sign, active) values (9, 1, 8, 'EAP01', true);
insert into resources (id, version, type_id, call_sign, active) values (10, 1, 9, 'EAP121', true);
insert into resources (id, version, type_id, call_sign, active) values (11, 1, 10, 'EAP131', true);

insert into resource_status (version, resource_id, state, available, assigned, ts) values (1, 1, 'OUT_OF_SERVICE', false, false, now());
insert into resource_status (version, resource_id, state, available, assigned, ts) values (1, 2, 'OUT_OF_SERVICE', false, false, now());
insert into resource_status (version, resource_id, state, available, assigned, ts) values (1, 3, 'OUT_OF_SERVICE', false, false, now());
insert into resource_status (version, resource_id, state, available, assigned, ts) values (1, 4, 'OUT_OF_SERVICE', false, false, now());
insert into resource_status (version, resource_id, state, available, assigned, ts) values (1, 5, 'OUT_OF_SERVICE', false, false, now());
insert into resource_status (version, resource_id, state, available, assigned, ts) values (1, 6, 'OUT_OF_SERVICE', false, false, now());
insert into resource_status (version, resource_id, state, available, assigned, ts) values (1, 7, 'OUT_OF_SERVICE', false, false, now());
insert into resource_status (version, resource_id, state, available, assigned, ts) values (1, 8, 'OUT_OF_SERVICE', false, false, now());
insert into resource_status (version, resource_id, state, available, assigned, ts) values (1, 9, 'OUT_OF_SERVICE', false, false, now());
insert into resource_status (version, resource_id, state, available, assigned, ts) values (1, 10, 'OUT_OF_SERVICE', false, false, now());
insert into resource_status (version, resource_id, state, available, assigned, ts) values (1, 11, 'OUT_OF_SERVICE', false, false, now());
