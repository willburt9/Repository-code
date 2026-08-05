/******************************************************************************
 * Projet 11 - OpenClassrooms
 *
 * Données d'initialisation de la base H2.
 *
 * Contenu :
 *  - Groupes de spécialités NHS
 *  - Spécialités médicales NHS
 *  - Hôpitaux de démonstration
 *  - Associations entre hôpitaux et spécialités
 ******************************************************************************/

-------------------------------------------------------------------------------
-- GROUPES DE SPÉCIALITÉS
-------------------------------------------------------------------------------

INSERT INTO groupe_specialite (id, nom) VALUES
(1, 'Anesthésie'),
(2, 'Oncologie clinique'),
(3, 'Groupe dentaire'),
(4, 'Médecine d''urgence'),
(5, 'Groupe de médecine générale'),
(6, 'Obstétrique et gynécologie'),
(7, 'Groupe pédiatrique'),
(8, 'Groupe de pathologie'),
(9, 'Groupe Pronostics et gestion de la santé/Santé communautaire'),
(10, 'Groupe de psychiatrie'),
(11, 'Groupe de radiologie'),
(12, 'Groupe chirurgical');

-------------------------------------------------------------------------------
-- SPÉCIALITÉS NHS
-------------------------------------------------------------------------------

INSERT INTO specialite (id, nom, groupe_id) VALUES
(101,'Anesthésie',1),
(102,'Soins intensifs',1),

(103,'Oncologie clinique',2),

(104,'Spécialités dentaires supplémentaires',3),
(105,'Radiologie dentaire et maxillo-faciale',3),
(106,'Endodontie',3),
(107,'Chirurgie buccale et maxillo-faciale',3),
(108,'Pathologie buccale et maxillo-faciale',3),
(109,'Médecine buccale',3),
(110,'Chirurgie buccale',3),
(111,'Orthodontie',3),
(112,'Dentisterie pédiatrique',3),
(113,'Parodontie',3),
(114,'Prosthodontie',3),
(115,'Dentisterie restauratrice',3),
(116,'Dentisterie de soins spéciaux',3),

(117,'Médecine d''urgence',4),

(118,'Médecine interne de soins aigus',5),
(119,'Allergie',5),
(120,'Médecine audiovestibulaire',5),
(121,'Cardiologie',5),
(122,'Génétique clinique',5),
(123,'Neurophysiologie clinique',5),
(124,'Pharmacologie clinique et thérapeutique',5),
(125,'Dermatologie',5),
(126,'Endocrinologie et diabète sucré',5),
(127,'Gastroentérologie',5),
(128,'Médecine générale (interne)',5),
(129,'Médecine générale',5),
(130,'Médecine générale (GP) 6 mois',5),
(131,'Médecine génito-urinaire',5),
(132,'Médecine gériatrique',5),
(133,'Maladies infectieuses',5),
(134,'Oncologie médicale',5),
(135,'Ophtalmologie médicale',5),
(136,'Neurologie',5),
(137,'Médecine du travail',5),
(138,'Autre',5),
(139,'Médecine palliative',5),
(140,'Médecine de réadaptation',5),
(141,'Médecine rénale',5),
(142,'Médecine respiratoire',5),
(143,'Rhumatologie',5),
(144,'Médecine du sport et de l''exercice',5),

(145,'Santé publique sexuelle et procréative',6),

(146,'Cardiologie pédiatrique',7),
(147,'Pédiatrie',7),

(148,'Pathologie chimique',8),
(149,'Neuropathologie diagnostique',8),
(150,'Histopathologie médico-légale',8),
(151,'Pathologie générale',8),
(152,'Hématologie',8),
(153,'Histopathologie',8),
(154,'Immunologie',8),
(155,'Microbiologie médicale',8),
(156,'Pathologie pédiatrique et périnatale',8),
(157,'Virologie',8),

(158,'Service de santé communautaire dentaire',9),
(159,'Service de santé communautaire médicale',9),
(160,'Santé publique dentaire',9),
(161,'Pratique de l’art dentaire',9),
(162,'Santé publique',9),

(163,'Psychiatrie infantile et adolescente',10),
(164,'Psychiatrie légale',10),
(165,'Psychiatrie générale',10),
(166,'Psychiatrie de la vieillesse',10),
(167,'Psychiatrie des troubles d''apprentissage',10),
(168,'Psychothérapie',10),

(169,'Radiologie clinique',11),
(170,'Médecine nucléaire',11),

(171,'Chirurgie cardiothoracique',12),
(172,'Chirurgie générale',12),
(173,'Neurochirurgie',12),
(174,'Ophtalmologie',12),
(175,'Otolaryngologie',12),
(176,'Chirurgie pédiatrique',12),
(177,'Chirurgie plastique',12),
(178,'Traumatologie et chirurgie orthopédique',12),
(179,'Urologie',12),
(180,'Chirurgie vasculaire',12);

-------------------------------------------------------------------------------
-- HÔPITAUX
-------------------------------------------------------------------------------

INSERT INTO hopital (
    id,
    nom,
    adresse,
    latitude,
    longitude,
    lits_disponibles
) VALUES
(1,'Hôpital Pitié-Salpêtrière','47-83 Boulevard de l''Hôpital, 75013 Paris',48.8383,2.3651,25),
(2,'Hôpital Européen Georges-Pompidou','20 Rue Leblanc, 75015 Paris',48.8399,2.2728,18),
(3,'Hôpital Necker-Enfants malades','149 Rue de Sèvres, 75015 Paris',48.8462,2.3150,12),
(4,'Hôpital Cochin','27 Rue du Faubourg Saint-Jacques, 75014 Paris',48.8397,2.3386,15),
(5,'Hôpital Saint-Louis','1 Avenue Claude Vellefaux, 75010 Paris',48.8763,2.3697,10),
(6,'Hôpital Lariboisière','2 Rue Ambroise Paré, 75010 Paris',48.8807,2.3553,9),
(7,'Hôpital Bichat-Claude Bernard','46 Rue Henri Huchard, 75018 Paris',48.8993,2.3299,16),
(8,'Hôpital Saint-Antoine','184 Rue du Faubourg Saint-Antoine, 75012 Paris',48.8472,2.3832,11),
(9,'Hôpital Robert-Debré','48 Boulevard Sérurier, 75019 Paris',48.8795,2.4027,14),
(10,'Hôpital Tenon','4 Rue de la Chine, 75020 Paris',48.8710,2.4008,8);

-------------------------------------------------------------------------------
-- SPÉCIALITÉS PROPOSÉES PAR LES HÔPITAUX
-------------------------------------------------------------------------------

INSERT INTO hopital_specialite (hopital_id, specialite_id) VALUES
(1,101),(1,117),(1,121),(1,127),(1,136),(1,169),(1,172),
(2,103),(2,121),(2,134),(2,169),(2,171),
(3,146),(3,147),(3,121),(3,163),(3,172),(3,176),(3,178),
(4,102),(4,121),(4,127),(4,172),
(5,134),(5,152),(5,155),(5,169),(5,172),
(6,117),(6,169),(6,116),(6,105),
(7,142),(7,172),(7,180),
(8,121),(8,172),(8,111),(8,170),
(9,102),(9,146),(9,147),(9,165),
(10,136),(10,178),(10,113),(10,171);