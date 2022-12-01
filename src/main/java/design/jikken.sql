CREATE TABLE Users(
	User_id INTEGER,
	Nickname VARCHAR(64),
	PRIMARY KEY(User_id)
);

CREATE TABLE History(
	Searched_at DATETIME,
	word VARCHAR(64),
	User_id INTEGER,
	FOREIGN KEY (User_id) REFERENCES Users (User_id)
);
--double(桁数,小数点以下の桁数)
CREATE TABLE Favorites(
	word VARCHAR(64),
	latitude DOUBLE(9,6),
	longitude DOUBLE(9,6),
	User_id INTEGER,
	FOREIGN KEY (User_id) REFERENCES Users (User_id)
);