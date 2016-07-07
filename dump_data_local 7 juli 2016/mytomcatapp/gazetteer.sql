-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               5.6.26 - MySQL Community Server (GPL)
-- Server OS:                    Win32
-- HeidiSQL Version:             7.0.0.4312
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

-- Dumping structure for table mytomcatapp.gazetteer
DROP TABLE IF EXISTS `gazetteer`;
CREATE TABLE IF NOT EXISTS `gazetteer` (
  `id_gazetteer` int(10) NOT NULL AUTO_INCREMENT,
  `location` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id_gazetteer`)
) ENGINE=InnoDB AUTO_INCREMENT=628 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table mytomcatapp.gazetteer: ~605 rows (approximately)
DELETE FROM `gazetteer`;
/*!40000 ALTER TABLE `gazetteer` DISABLE KEYS */;
INSERT INTO `gazetteer` (`id_gazetteer`, `location`) VALUES
	(1, 'a'),
	(2, 'aceh'),
	(3, 'achmad'),
	(4, 'ad'),
	(5, 'afrika'),
	(6, 'agung'),
	(7, 'ahmad'),
	(8, 'alam'),
	(9, 'alfalink'),
	(10, 'alsi'),
	(11, 'amarosa'),
	(12, 'ambu'),
	(14, 'angklung'),
	(15, 'antropark'),
	(16, 'arboretum'),
	(17, 'arcamanik'),
	(18, 'ardan'),
	(19, 'ardjuna'),
	(20, 'area'),
	(21, 'arena'),
	(22, 'arsitektur'),
	(23, 'art'),
	(24, 'asia'),
	(25, 'asri'),
	(26, 'aston'),
	(27, 'atrium'),
	(28, 'audio'),
	(29, 'auditorium'),
	(30, 'aula'),
	(31, 'baja'),
	(32, 'bajuri'),
	(33, 'bakti'),
	(34, 'balai'),
	(35, 'balaikota'),
	(36, 'bale'),
	(37, 'bali'),
	(38, 'balkot'),
	(39, 'ballroom'),
	(40, 'banda'),
	(41, 'bandung'),
	(42, 'bank'),
	(43, 'bar'),
	(44, 'barat'),
	(45, 'baros'),
	(46, 'baru'),
	(47, 'basket'),
	(48, 'batu'),
	(49, 'batununggal'),
	(50, 'bcc'),
	(51, 'beat'),
	(52, 'beberapa'),
	(53, 'belakang'),
	(54, 'belitung'),
	(55, 'berlian'),
	(56, 'bf'),
	(57, 'bhakti'),
	(58, 'bina'),
	(59, 'binamarga'),
	(60, 'binekas'),
	(61, 'binus'),
	(62, 'bip'),
	(63, 'blok'),
	(64, 'bober'),
	(65, 'boutique'),
	(66, 'bp'),
	(67, 'bpu'),
	(68, 'braga'),
	(69, 'btc'),
	(70, 'buah'),
	(71, 'buana'),
	(72, 'budaya'),
	(73, 'building'),
	(74, 'bukit'),
	(75, 'bumi'),
	(76, 'butcherâ€™s'),
	(77, 'cafe'),
	(78, 'cahya'),
	(79, 'california'),
	(80, 'camp'),
	(81, 'campus'),
	(82, 'car'),
	(85, 'cempaka'),
	(86, 'center'),
	(87, 'centre'),
	(88, 'centrum'),
	(89, 'christian'),
	(90, 'cibereum'),
	(91, 'cibeunying'),
	(92, 'cihampelas'),
	(93, 'cikutra'),
	(94, 'cimahi'),
	(95, 'cinta'),
	(96, 'cipamokolan'),
	(97, 'cipedes'),
	(98, 'cisangkuy'),
	(99, 'cisokan'),
	(100, 'citarum'),
	(101, 'city'),
	(102, 'citylink'),
	(103, 'citywalk'),
	(104, 'ciumbuleuit'),
	(105, 'ciumbulueit'),
	(106, 'ciwalk'),
	(107, 'clarity'),
	(108, 'clcc'),
	(109, 'co'),
	(111, 'conv'),
	(112, 'convention'),
	(113, 'conventions'),
	(114, 'core'),
	(115, 'cream'),
	(116, 'creative'),
	(117, 'ctra'),
	(118, 'dago'),
	(119, 'damri'),
	(121, 'darul'),
	(122, 'darussalam'),
	(123, 'dasar'),
	(124, 'day'),
	(125, 'dayang'),
	(126, 'dayeuh'),
	(127, 'de'),
	(128, 'delegation'),
	(129, 'demang'),
	(130, 'denpom'),
	(131, 'des'),
	(132, 'destinasi'),
	(133, 'di'),
	(134, 'diii'),
	(135, 'diklat'),
	(136, 'dikumpulkan'),
	(137, 'dinas'),
	(138, 'dipati'),
	(139, 'dipatiukur'),
	(140, 'diponegoro'),
	(141, 'direktorat'),
	(142, 'disjas'),
	(143, 'djuanda'),
	(144, 'djunjunan'),
	(145, 'dkv'),
	(146, 'dome'),
	(147, 'dr.'),
	(148, 'dr.setiabudhi'),
	(149, 'drg.'),
	(150, 'du'),
	(151, 'dâ€™indonesie'),
	(152, 'eatery'),
	(154, 'edelweis'),
	(155, 'eduplex'),
	(156, 'ekonomi'),
	(157, 'eks'),
	(158, 'ekuitas'),
	(159, 'electrical'),
	(160, 'engineer'),
	(161, 'enhaii'),
	(162, 'exkopma'),
	(163, 'exmansion'),
	(165, 'eyckman'),
	(166, 'fabrik'),
	(167, 'factory'),
	(168, 'fakultas'),
	(169, 'famestation'),
	(170, 'faperta'),
	(172, 'fave'),
	(173, 'favehotel'),
	(174, 'fe'),
	(175, 'feb'),
	(176, 'ferucci'),
	(177, 'feruci'),
	(178, 'field'),
	(179, 'fisip'),
	(180, 'fit'),
	(181, 'floor'),
	(182, 'flower'),
	(183, 'formulir'),
	(185, 'fpbs'),
	(186, 'fptk'),
	(187, 'francais'),
	(189, 'from'),
	(190, 'front'),
	(191, 'fsrd'),
	(192, 'fsrditb'),
	(193, 'futsal'),
	(194, 'g'),
	(195, 'galeri'),
	(196, 'gallery'),
	(197, 'ganeca'),
	(198, 'ganesha'),
	(199, 'garuda'),
	(200, 'gas'),
	(201, 'gd'),
	(202, 'gd.'),
	(203, 'gd.fakultas'),
	(204, 'gd.wahana'),
	(205, 'gedung'),
	(206, 'gegerkalong'),
	(207, 'gf'),
	(208, 'gigi'),
	(209, 'gino'),
	(210, 'gk.'),
	(211, 'golden'),
	(212, 'goodmorning'),
	(213, 'gor'),
	(214, 'graha'),
	(215, 'grand'),
	(216, 'gratis!!'),
	(217, 'grha'),
	(218, 'ground'),
	(219, 'gsg'),
	(220, 'guide'),
	(221, 'guna'),
	(222, 'gunung'),
	(223, 'gymnasium'),
	(224, 'h'),
	(225, 'h.'),
	(226, 'halaman'),
	(227, 'hall'),
	(228, 'hardjadinata'),
	(229, 'hardjakusuma'),
	(230, 'hardrockers'),
	(231, 'harris'),
	(232, 'hasanudin'),
	(233, 'hatta'),
	(234, 'hewan'),
	(235, 'hidayat'),
	(236, 'hikam'),
	(237, 'hilton'),
	(239, 'holiday'),
	(240, 'hotel'),
	(241, 'house'),
	(242, 'house)'),
	(243, 'huab'),
	(244, 'hukum'),
	(245, 'hutan'),
	(246, 'i'),
	(247, 'ifi'),
	(248, 'ii'),
	(249, 'iii'),
	(250, 'ilmu'),
	(251, 'indah'),
	(252, 'indonesia'),
	(253, 'infobdg'),
	(254, 'infokom'),
	(255, 'inhoftank'),
	(256, 'inn'),
	(257, 'insan'),
	(258, 'institut'),
	(259, 'internasional'),
	(260, 'ir'),
	(261, 'ir.'),
	(262, 'ir.h.djuanda'),
	(263, 'ir.h.juanda'),
	(264, 'isi'),
	(265, 'ismail'),
	(266, 'itb'),
	(267, 'itenas'),
	(268, 'ithb'),
	(269, 'iv'),
	(270, 'ixb'),
	(271, 'jabar'),
	(272, 'jakarta'),
	(273, 'jalan'),
	(274, 'januari'),
	(275, 'japati'),
	(276, 'jatinangor'),
	(277, 'java'),
	(278, 'jawa'),
	(279, 'jembatan'),
	(280, 'jenderal'),
	(281, 'jl'),
	(282, 'jl.'),
	(283, 'jln.'),
	(284, 'jln'),
	(285, 'juanda'),
	(286, 'june'),
	(287, 'juni'),
	(288, 'kaa'),
	(289, 'kampus'),
	(290, 'kandaga'),
	(291, 'kantor'),
	(292, 'karya'),
	(293, 'katolik'),
	(294, 'kawasan'),
	(295, 'ke'),
	(296, 'kebonjati'),
	(297, 'kebudayaan'),
	(298, 'kemenkes'),
	(299, 'kitchen'),
	(300, 'klinik'),
	(301, 'km'),
	(302, 'kolot'),
	(303, 'komplek'),
	(304, 'konferensi'),
	(305, 'kopma'),
	(306, 'kopo'),
	(307, 'kosambi'),
	(308, 'kota'),
	(309, 'kpad'),
	(310, 'krida'),
	(311, 'labtek'),
	(312, 'landmark'),
	(313, 'langlangbuana'),
	(314, 'lantai'),
	(315, 'lap.'),
	(316, 'lapangan'),
	(317, 'lawangwangi'),
	(318, 'layang'),
	(319, 'layar'),
	(320, 'learning'),
	(321, 'lembang'),
	(322, 'lengkong'),
	(323, 'light'),
	(324, 'lingkar'),
	(325, 'link'),
	(326, 'lobby'),
	(327, 'lodaya'),
	(328, 'loop'),
	(329, 'lot'),
	(330, 'loubelle'),
	(331, 'lounge'),
	(332, 'lt'),
	(333, 'lt.'),
	(334, 'm'),
	(335, 'main'),
	(336, 'maja'),
	(337, 'mall'),
	(338, 'manajemen'),
	(339, 'mandalawangi'),
	(340, 'mandiri'),
	(341, 'manggala'),
	(342, 'maranatha'),
	(343, 'martadinata'),
	(344, 'masjid'),
	(345, 'maxiâ€™s'),
	(346, 'mayang'),
	(347, 'memphis'),
	(348, 'menarik'),
	(349, 'merah'),
	(350, 'merdeka'),
	(351, 'mesjid'),
	(352, 'metro'),
	(353, 'miko'),
	(354, 'mind'),
	(355, 'miracle'),
	(356, 'mitra'),
	(357, 'monkey'),
	(358, 'monumen'),
	(359, 'mulia'),
	(360, 'mulut'),
	(361, 'museum'),
	(362, 'musik'),
	(363, 'mustafa'),
	(364, 'mustofa'),
	(365, 'negara'),
	(366, 'nisp'),
	(367, 'no'),
	(368, 'no.'),
	(369, 'no.a'),
	(370, 'ocbc'),
	(372, 'office'),
	(373, 'offline'),
	(374, 'online'),
	(375, 'open'),
	(376, 'operation'),
	(377, 'otista'),
	(378, 'outlet'),
	(379, 'p'),
	(380, 'p.h.h'),
	(381, 'padasuka'),
	(382, 'padepokan'),
	(383, 'padjadjaran'),
	(384, 'pajajaran'),
	(385, 'pakar'),
	(386, 'palais'),
	(387, 'pangkalan'),
	(388, 'parahyangan'),
	(389, 'pare'),
	(390, 'paris'),
	(391, 'parisj'),
	(392, 'parijs'),
	(393, 'pariwisata'),
	(394, 'park'),
	(395, 'parking'),
	(396, 'parkir'),
	(397, 'pascasarjana'),
	(398, 'pasirluyu'),
	(399, 'pasirwangi'),
	(400, 'passion'),
	(401, 'pasteur'),
	(402, 'pasundan'),
	(403, 'pasupati'),
	(404, 'pedro'),
	(405, 'pejuang'),
	(406, 'pelajar'),
	(407, 'pelataran'),
	(408, 'pelindung'),
	(409, 'pemkot'),
	(410, 'pemerintah'),
	(411, 'pendidikan'),
	(412, 'pendopo'),
	(413, 'perjuangan'),
	(414, 'perky'),
	(415, 'permata'),
	(416, 'pertanian'),
	(417, 'perusahaan'),
	(418, 'peta'),
	(419, 'phh'),
	(420, 'phh.'),
	(421, 'piset'),
	(422, 'plasa'),
	(423, 'plaza'),
	(424, 'pojok'),
	(425, 'polban'),
	(426, 'polman'),
	(427, 'poltekkes'),
	(428, 'pomdam'),
	(429, 'pos'),
	(430, 'ppsdal'),
	(431, 'prakarya'),
	(432, 'prama'),
	(433, 'pramuka'),
	(434, 'preanger'),
	(435, 'primera'),
	(436, 'prof.'),
	(437, 'progo'),
	(438, 'pu'),
	(439, 'public'),
	(440, 'purnawarman'),
	(441, 'pusat'),
	(442, 'pusdai'),
	(443, 'pusdikif'),
	(444, 'puspa'),
	(445, 'pussenif'),
	(446, 'putih'),
	(447, 'putra'),
	(448, 'pvj'),
	(449, 'r.e.'),
	(450, 'rakyat'),
	(451, 'ramayana'),
	(452, 'raya'),
	(453, 'rd'),
	(454, 're.'),
	(455, 'registration'),
	(456, 'rektorat'),
	(457, 'resort'),
	(458, 'resources'),
	(459, 'resto'),
	(460, 'riau'),
	(461, 'rocca'),
	(462, 'room'),
	(463, 'rs'),
	(464, 'rshs'),
	(465, 'ruang'),
	(466, 'ruko'),
	(467, 'rumah'),
	(468, 'rumawat'),
	(470, 'sabuga'),
	(471, 'sahabat'),
	(472, 'sakit'),
	(473, 'salak'),
	(474, 'salis'),
	(475, 'salman'),
	(476, 'samaya'),
	(477, 'sangga'),
	(478, 'sangkuriang'),
	(479, 'santika'),
	(480, 'sanusi'),
	(481, 'saparua'),
	(482, 'saridiredja'),
	(483, 'sasana'),
	(484, 'saung'),
	(485, 'saungaji'),
	(486, 'school'),
	(487, 'science'),
	(488, 'sebandung'),
	(489, 'sederahana'),
	(490, 'sekeloa'),
	(491, 'sekolah'),
	(492, 'sekretariat'),
	(493, 'selasar'),
	(494, 'selatan'),
	(496, 'senayan'),
	(497, 'seni'),
	(498, 'sepanjang'),
	(499, 'serba'),
	(500, 'serbaguna'),
	(501, 'serela'),
	(502, 'sersan'),
	(503, 'setiabudhi'),
	(504, 'setiabudi'),
	(505, 'shop'),
	(507, 'sidomukti'),
	(508, 'siliwangi'),
	(509, 'sma'),
	(510, 'sman'),
	(511, 'smith'),
	(512, 'smk'),
	(513, 'smkn'),
	(514, 'smpn'),
	(515, 'sobbers'),
	(516, 'sobers'),
	(517, 'soekarno'),
	(518, 'softball'),
	(519, 'sor'),
	(520, 'spa'),
	(521, 'space'),
	(522, 'sport'),
	(523, 'square'),
	(524, 'st'),
	(525, 'stadion'),
	(527, 'station'),
	(528, 'stie'),
	(529, 'stinten'),
	(530, 'store'),
	(531, 'stp'),
	(532, 'stpb'),
	(533, 'stsi'),
	(534, 'stt'),
	(535, 'student'),
	(536, 'studio'),
	(537, 'study'),
	(538, 'suci'),
	(539, 'sugar'),
	(540, 'sulanjana'),
	(541, 'sultan'),
	(542, 'sumantri'),
	(543, 'sumatera'),
	(544, 'sumatra'),
	(545, 'sumbi'),
	(546, 'sumedang'),
	(547, 'sunan'),
	(548, 'sunaryo'),
	(549, 'sunda'),
	(550, 'supratman'),
	(551, 'suradiredja'),
	(552, 'surapati'),
	(553, 'surya'),
	(554, 'sutera'),
	(555, 't.e.s'),
	(556, 'taman'),
	(557, 'tamansari'),
	(558, 'tanginas'),
	(559, 'taruna'),
	(560, 'tea'),
	(561, 'teater'),
	(562, 'tegalega'),
	(563, 'tegallega'),
	(564, 'teknik'),
	(565, 'teknologi'),
	(566, 'tekstil'),
	(567, 'telekomunikasi'),
	(568, 'telkom'),
	(569, 'telkomsel'),
	(570, 'tempat'),
	(571, 'tengah'),
	(573, 'terapan'),
	(574, 'ters'),
	(575, 'ters.'),
	(576, 'tertutup'),
	(577, 'terusan'),
	(579, 'theater'),
	(580, 'theatre'),
	(581, 'timur'),
	(582, 'tinggi'),
	(583, 'tni'),
	(584, 'trans'),
	(585, 'tropica'),
	(586, 'trunojoyo'),
	(587, 'tubagus'),
	(588, 'tuik'),
	(589, 'udjo'),
	(590, 'ukur'),
	(591, 'unikom'),
	(592, 'uninus'),
	(593, 'unisba'),
	(594, 'universitas'),
	(595, 'university'),
	(596, 'unjani'),
	(597, 'unkl'),
	(598, 'unpad'),
	(599, 'unpar'),
	(600, 'unpas'),
	(601, 'upi'),
	(602, 'usbypkp'),
	(603, 'utama'),
	(604, 'utara'),
	(605, 'v'),
	(606, 'van'),
	(607, 'via'),
	(608, 'vio'),
	(611, 'vitri'),
	(612, 'wahana'),
	(613, 'wakaf'),
	(614, 'waras'),
	(615, 'wastukencana'),
	(616, 'widya'),
	(617, 'wijde'),
	(618, 'wisata'),
	(619, 'wisma'),
	(621, 'wr'),
	(623, 'xxi'),
	(624, 'yani'),
	(625, 'yayasan'),
	(626, 'ypk'),
	(627, 'ypkp');
/*!40000 ALTER TABLE `gazetteer` ENABLE KEYS */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
