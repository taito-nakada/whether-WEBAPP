package design;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class studentBean {

	private int id;
	private String name;
	private float gpa;
	
	public studentBean() {
		;
	}

	// ====================================
	// セッターメソッドとゲッターメソッド
	// ====================================

	// 学籍番号
	public	void setId(int i) {
		id = i;
	}
	public int getId() {
		return id;
	}
	// 氏名
	public void setName(String nm) {
		name = nm;
	}
	public String getName() {
		return name;
	}
	// GPA
	public	void setGpa(float g) {
		gpa = g;
	}
	public float getGpa() {
		return gpa;
	}
	
	// =====================================================
	// データベースへの追加
	//   setId(), setName(), setGpa() でセットした内容
	// =====================================================
	public boolean insertRecord() {
		
		try {
			// データベースへのコネクションを取得
			Connection con = DBManager.getUserConnection();
			//
			// SQL文 の実行例（１）: じかに　SQL 文を実行させる
			//
			// (1-1) SQL発行のためのステートメントを取得
			Statement smt = con.createStatement();
			// (1-2) SQL の実行
			int count = smt.executeUpdate(
					"INSERT INTO Student (Id,Name,Gpa) VALUES"
					+ "(" + id + ",'" + name + "'," + gpa + ")" );
			// (1-3)  コネクションを閉じる
			smt.close();
			con.close();
			// (1-4) executeUpdate の返り値には、更新された行数になるので
			// これを利用して、ただしく実行されたか確認
			if (count>0) 
				return true;
			else 
				return false;
		} catch (Exception e) {
			// もし、上の try ブロックで例外などのエラーが起きた場合
			// ここに自動的にジャンプしてくる
			e.printStackTrace();  // データベース接続の例外エラー表示
			return false;
		}
	}
	
	// ======================================================
	//  データベースからの削除
	//     setId() してから実行する
	// ======================================================
	public boolean deleteRecord() {
		
		try {
			// データベースへのコネクションを取得
			Connection con = DBManager.getUserConnection();
			//
			// SQL文 の実行例（２）: 間接的に　SQL 文を実行させる
			//
			// (2-1) SQL文の骨格を用意しておく（？部分が可変になる）
			String sql = "DELETE FROM Student Where Id=?";
			//  (2-2)  プリペアード（あらかじめ準備された）ステートメントを取得する
			PreparedStatement ps = con.prepareStatement(sql);
			//  (2-3) ステートメントの該当位置に値をセットする
			//     文字列の場合には ps.setString() を使う
			ps.setInt(1,id);
			// (2-4) SQLを実行する
			int count = ps.executeUpdate();
			// (2-5) コネクションを閉じる
			ps.close();
			con.close();			
			// (2-6) executeUpdate の返り値には、更新された行数になるので
			// これを利用して、ただしく実行されたか確認
			if (count>0) 
				return true;
			else 
				return false;
		} catch (Exception e) {
			// もし、上の try ブロックで例外などのエラーが起きた場合
			// ここに自動的にジャンプしてくる
			e.printStackTrace(); // データベース接続の例外エラー表示
			return false;
		}
	}
	
	// =============================
	//  データベースから一覧を取得
	// =============================
	public ArrayList<studentBean> getUserRecords() {

		ArrayList<studentBean> list = new ArrayList<studentBean>();

		try {
			// データベースへのコネクションを取得
			Connection con = DBManager.getUserConnection();
			//
			// SQL文 の実行例（３）: executeQuery による検索
			//
			// (3-1) ステートメントを取得する
			Statement smt = con.createStatement();
			// (3-2) SELECT を実行して、結果を ResultSet に入れる
			ResultSet rs = smt.executeQuery("SELECT * FROM Student");
			// (3-3) リストに結果を格納する
			//  かなら以下のループが必要である（少なくとも１回は resultSetクラスの
			//   next メソッドを呼ばないと最初の要素を取り出すことができない）
			//    SELECT 文から返された複数行の結果を１行ずつ取得するループである。
			while( rs.next() ) {
				studentBean tmpSb = new studentBean();
				tmpSb.setId( rs.getInt("Id") );
				tmpSb.setName( rs.getString("Name") );
				tmpSb.setGpa( rs.getFloat("Gpa") );
				// StudentBean インスタンスを配列型リスト（ArrayList）に追加していく
				list.add(tmpSb);
			}
			// (3-4) コネクションを閉じる
			rs.close();
			smt.close();
			con.close();
			// (3-5) 最後に、得られた配列型リスト（各要素は StudentBeanインスタンス）を結果を返す
			return list;
		} catch (Exception e) {
			// もし、上の try ブロックで例外などのエラーが起きた場合
			// ここに自動的にジャンプしてくる
			e.printStackTrace(); // データベース接続の例外エラー表示
			return null;
		}
	}
}
