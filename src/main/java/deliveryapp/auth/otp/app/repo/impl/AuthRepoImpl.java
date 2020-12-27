package deliveryapp.auth.otp.app.repo.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import deliveryapp.auth.otp.app.repo.AuthRepo;


@Repository
public class AuthRepoImpl implements AuthRepo {

	@Autowired
	@Qualifier("mappingNamedParameterJdbcTemplate")
	NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	 public String checkIfNewUser(String mobile) {
		 
		 Integer count;
		 Integer affectedRows = 0;
		 
		 String SQL = "select count(*) from users where mobile=:mobile";
		 
			try {
				 count = namedParameterJdbcTemplate.queryForObject(SQL, new MapSqlParameterSource().addValue("mobile",mobile) ,
						Integer.class);
				 
			}
			catch(Exception e) {
				e.printStackTrace();
				return null;
			}
			
			// Case1: if count is 0 then new user so insertion and return resultstatus with new user
		if(count == 0) {
	    String SQL1 = "Insert into users values(:mobile,:name)";
			
			 affectedRows = namedParameterJdbcTemplate.update(SQL1, new MapSqlParameterSource().addValue("mobile", mobile)					.addValue("name", "testname"));
		}
		
			
			if(affectedRows>0) {
				return "new user";
			}

			
			//Case2: if count is 1 then user already exist
			
			return "old user";
			

	 }
}
