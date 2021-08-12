package ir.sharif.gamein2021.ClientHandler.authentication.util;

import com.google.gson.Gson;
import ir.sharif.gamein2021.ClientHandler.authentication.model.AuthenticationRequest;
import ir.sharif.gamein2021.ClientHandler.authentication.model.AuthenticationResponse;
import ir.sharif.gamein2021.ClientHandler.authentication.model.PlayerModel;
import ir.sharif.gamein2021.ClientHandler.controller.TeamController;
import ir.sharif.gamein2021.ClientHandler.view.ResponseObject;
import ir.sharif.gamein2021.core.util.RequestConstants;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SignatureException;

import static ir.sharif.gamein2021.core.util.RequestConstants.AuthenticateResponse;

@Component
public class AuthenticateHandler {
    private Gson gson;

    private AuthenticateHandler(TeamController teamController) {
        this.teamController = teamController;
        gson = new Gson();
    }

    private TeamController teamController;


    public ResponseObject<Object> authenticate(AuthenticationRequest request, String chance) {
        synchronized (this) {
            ResponseObject<Object> response;
            if (chance == null /*|| userPassCommands.get(request.getUsername()) == null */ || request.getPassAndSalt().get(1) == null) {
                System.out.println("Null parameter authenticate.");
                return new ResponseObject<>(RequestConstants.AuthenticateErrorResponse);
            }
//            String toHash = chance.concat(userPassCommands.get(request.getUsername())).concat(request.getPassAndSalt().get(1));
//            String databaseResponse = userPlayerCommand.get(request.getUsername());
            String toHash = "";
            String databaseResponse = "";
            if (databaseResponse == null) {
                response = new ResponseObject<>(RequestConstants.AuthenticateErrorResponse);
                return response;
            }
            PlayerModel playerModel = gson.fromJson(databaseResponse, PlayerModel.class);
            int playerId = playerModel.getPlayerId();
            String teamName = playerModel.getTeamName();
            try {
                String hashed = sha256Digest(toHash);
                if (hashed.equals(request.getPassAndSalt().get(0))) {
                    teamController.addPlayer(teamName, playerId);
                    response = new ResponseObject<>(AuthenticateResponse, new AuthenticationResponse(playerId));
                } else {
                    response = new ResponseObject<>(RequestConstants.AuthenticateErrorResponse);
                }
            } catch (SignatureException e) {
                response = new ResponseObject<>(RequestConstants.AuthenticateErrorResponse);
            }
            return response;
        }
    }

    public String sha256Digest(String data) throws SignatureException {
        return getDigest(data);
    }

    private String getDigest(String data)
            throws SignatureException {
        try {
            MessageDigest mac = MessageDigest.getInstance("SHA-256");
            mac.update(data.getBytes(StandardCharsets.UTF_8));
            return toHex(mac.digest()).toLowerCase();
        } catch (Exception e) {
            throw new SignatureException(e);
        }
    }

    private String toHex(byte[] bytes) {
        BigInteger bi = new BigInteger(1, bytes);
        return String.format("%0" + (bytes.length << 1) + "X", bi);
    }
}
