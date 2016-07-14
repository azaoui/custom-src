function ($location,$http,$scope) {
// var processHosrVar= 
 //http://localhost:8980/bonita/API/bpm/processParameter/5272403358680847825/webServiceHostName
// var vm = this;
 

  $scope.goSoldes = false; 
 
var getQueryString = function ( field, url ) {
    var href = url ? url : window.location.href;
    var reg = new RegExp( '[?&]' + field + '=([^&#]*)', 'i' );
    var string = reg.exec(href);
    return string ? string[1] : null;
};

var id = getQueryString('id');
var url='../bonita/API/bpm/processParameter/'+id+'/webServiceHostName';

var sessionUrl='../API/system/session/unusedid';
var userid;

$http.get(sessionUrl)
                .success(function(session) {
$http.get(url).success(function(data) {



$http.get(data.value+'/rest/conges/getSolde/'+session.user_name)
                .success(function(data) {
                    $scope.solde = eval(data);
                    console.log(data);
                })
                .error(function(data) {
                    console.log('Error: ' + data);
                }); 
 
 
$http.get(data.value+'/rest/conges/getSoldeCetHistorique/'+session.user_name)
                .success(function(historique) {
                    $scope.soldeHistorique = eval(historique);
                    console.log(historique);
                })
                .error(function(historique) {
                    console.log('Error: ' + historique);
                }); 
                
 $http.get(data.value+'/rest/conges/getSoldeCetPerenne/'+session.user_name)
                .success(function(perenne) {
                    $scope.soldePrenne = eval(perenne);
                    console.log(perenne);
                })
                .error(function(perenne) {
                    console.log('Error: ' + perenne);
                });  
 
             
                
                
       });
       
        
 });

///../API/system/session/unusedid



 

}
