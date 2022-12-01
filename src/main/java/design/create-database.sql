drop table Favorites;

--Favorites 	選択された場所の検索を行って表示するためのデータを取得する
CREATE TABLE Favorites(
	ID INTEGER AUTO_INCREMENT NOT NULL PRIMARY KEY,
	Name VARCHAR(64),
	Latitude DOUBLE(9,6),
	Longitude DOUBLE(9,6)
);

INSERT INTO Favorites (Longitude,Latitude,Name) VALUES (137.1489429473877,35.10354911705993,"豊田高専");

DROP TABLE Favorites;