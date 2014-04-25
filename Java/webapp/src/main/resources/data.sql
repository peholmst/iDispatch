insert into municipalities (version, name) values (1, 'Pargas');
insert into municipalities (version, name) values (1, 'Kimitoön');

insert into ticket_types (version, code, description) values (1, '103', 'Brandlarm');
insert into ticket_types (version, code, description) values (1, '202', 'Trafikolycka: liten');
insert into ticket_types (version, code, description) values (1, '203', 'Trafikolycka: medelstor');
insert into ticket_types (version, code, description) values (1, '401', 'Byggnadsbrand: liten');
insert into ticket_types (version, code, description) values (1, '402', 'Byggnadsbrand: medelstor');
insert into ticket_types (version, code, description) values (1, '411', 'Fordonsbrand: liten');
insert into ticket_types (version, code, description) values (1, '412', 'Fordonsbrand: medelstor');
insert into ticket_types (version, code, description) values (1, '421', 'Terrängbrand: liten');
insert into ticket_types (version, code, description) values (1, '422', 'Terrängbrand: medelstor');
insert into ticket_types (version, code, description) values (1, '431', 'Annan brand: liten');
insert into ticket_types (version, code, description) values (1, '432', 'Annan brand: medelstor');

insert into resource_types (id, version, code, description) values (1, 1, 'RP2', 'Jourhavande chef');
insert into resource_types (id, version, code, description) values (2, 1, 'RP3', 'Jourhavande befäl');
insert into resource_types (id, version, code, description) values (3, 1, 'R1', 'Släckningsbil');
insert into resource_types (id, version, code, description) values (4, 1, 'R2', 'Tank-släckningsbil');
insert into resource_types (id, version, code, description) values (5, 1, 'R3', 'Tankbil');
insert into resource_types (id, version, code, description) values (6, 1, 'R7', 'Manskapsbil');
insert into resource_types (id, version, code, description) values (7, 1, 'E0', 'Läkarenhet');
insert into resource_types (id, version, code, description) values (8, 1, 'E1', 'Akutvårdens fältchef');
insert into resource_types (id, version, code, description) values (9, 1, 'E2', 'Ambulans vårdnivå');
insert into resource_types (id, version, code, description) values (10, 1, 'E3', 'Ambulans grundnivå');

insert into resources (id, type_id, call_sign, active) values (1, 1, 'RAP20', true);
insert into resources (id, type_id, call_sign, active) values (2, 2, 'RAP30', true);
insert into resources (id, type_id, call_sign, active) values (3, 3, 'RAP101', true);
insert into resources (id, type_id, call_sign, active) values (4, 3, 'RAP201', true);
insert into resources (id, type_id, call_sign, active) values (5, 4, 'RAP302', true);
insert into resources (id, type_id, call_sign, active) values (6, 5, 'RAP103', true);
insert into resources (id, type_id, call_sign, active) values (7, 6, 'RAP207', true);
insert into resources (id, type_id, call_sign, active) values (8, 7, 'EAP00', true);
insert into resources (id, type_id, call_sign, active) values (9, 8, 'EAP01', true);
insert into resources (id, type_id, call_sign, active) values (10, 9, 'EAP121', true);
insert into resources (id, type_id, call_sign, active) values (11, 10, 'EAP131', true);
