import bizdata.EXOUsers

import org.bonitasoft.engine.api.IdentityAPI
import org.bonitasoft.engine.identity.User
import org.bonitasoft.engine.identity.Group
import org.bonitasoft.engine.identity.Role
import org.bonitasoft.engine.identity.UserCriterion
import org.bonitasoft.engine.identity.UserNotFoundException
import org.bonitasoft.engine.identity.GroupNotFoundException
import org.bonitasoft.engine.identity.RoleNotFoundException
import org.bonitasoft.engine.profile.Profile;
import org.bonitasoft.engine.profile.ProfileMember;
import org.bonitasoft.engine.profile.ProfileMemberCreator;
import org.bonitasoft.engine.profile.ProfileMemberSearchDescriptor;
import org.bonitasoft.engine.profile.ProfileSearchDescriptor;
import org.bonitasoft.engine.search.SearchOptionsBuilder;
import org.bonitasoft.engine.search.SearchResult;
import org.json.JSONObject
import org.json.JSONArray

import java.util.logging.Logger;
Logger logger= Logger.getLogger("org.bonitasoft");


/*create by azaoui@exoplatform.com
  dev.zaouiahmed@gmail.com
*/


String userss =new URL(webServiceHostName+"/rest/conges/getAllUsers")
.getText(connectTimeout: 5000,
		readTimeout: 10000,
		useCaches: true,
		allowUserInteraction: false,
		requestProperties: ['Connection': 'close'])
		  
JSONArray usersArray = new JSONArray(userss);
			// JSONObject jsonObject = new JSONObject(userss);
			// JSONArray jsonObject = jsonObject.getJSONArray("userName");


EXOUsers va = new EXOUsers()
va.users=userss

IdentityAPI identityAPI = apiAccessor.identityAPI

List<User> users

// Offset and increment to use when using paginated API
int offset = 0
int increment = 10

//logger.severe(usersArray.getJSONObject(0).get("userName"));

// For each user


for(int i = 0 ; i < usersArray.length() ; i++){

try
{
	identityAPI.getUserByUserName(usersArray.getJSONObject(i).get("userName").toString())
} catch (UserNotFoundException e) {

logger.severe("-----NOT founddddddddddddd---->"+usersArray.getJSONObject(i).get("userName").toString())

//User user= identityAPI.createUser(usersArray.getJSONObject(i).get("userName"), usersArray.getJSONObject(i).get("password"))

User user= identityAPI.createUser(usersArray.getJSONObject(i).get("userName"), usersArray.getJSONObject(i).get("password"), usersArray.getJSONObject(i).get("firstname"), usersArray.getJSONObject(i).get("lastname"))



try {
	
	Group defaultGroup = identityAPI.getGroupByPath("/acme");
	Role defaultRole = identityAPI.getRoleByName("member");
	
	
	
	identityAPI.addUserMembership(user.getId(), defaultGroup.getId(),defaultRole.getId());

}
catch (GroupNotFoundException e1) {
			logger.severe("GROUP NOT FOUND")
			} catch (RoleNotFoundException e2) {
			logger.severe("ROLE NOT FOUND")
			}catch (Exception e3) {
			logger.severe("can't create role")
			}


org.bonitasoft.engine.api.ProfileAPI orgProfileAPI = apiAccessor.getProfileAPI();
SearchOptionsBuilder searchOptionsBuilder = new SearchOptionsBuilder(0,10);
searchOptionsBuilder.filter(ProfileSearchDescriptor.NAME, "user");
SearchResult<Profile> searchResultProfile = orgProfileAPI.searchProfiles(searchOptionsBuilder.done());
	
// we should find one result now
if (searchResultProfile.getResult().size()!=1)
		{ return; }

// now register the user in the profile
Profile profile = searchResultProfile.getResult().get(0);
ProfileMemberCreator profileMemberCreator = new ProfileMemberCreator( profile.getId());
profileMemberCreator.setUserId( user.getId());
orgProfileAPI.createProfileMember(profileMemberCreator);


}



}


return va