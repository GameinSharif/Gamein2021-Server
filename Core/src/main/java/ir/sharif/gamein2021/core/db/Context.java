package ir.sharif.gamein2021.core.db;

import ir.sharif.gamein2021.core.db.dbAccessors.*;

public class Context {

    public UserDBAccessor userDBAccessor = new UserDBAccessor();
    public TeamDBAccessor teamDBAccessor = new TeamDBAccessor();
    public OfferDBAccessor offerDBAccessor = new OfferDBAccessor();

}
