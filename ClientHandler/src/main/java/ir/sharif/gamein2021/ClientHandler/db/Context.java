package ir.sharif.gamein2021.ClientHandler.db;

import ir.sharif.gamein2021.ClientHandler.db.dbAccessors.*;

public class Context {

    public UserDBAccessor userDBAccessor = new UserDBAccessor();
    public TeamDBAccessor teamDBAccessor = new TeamDBAccessor();
    public OfferDBAccessor offerDBAccessor = new OfferDBAccessor();

}
