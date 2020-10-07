package com.phoebus.appdemowallet.services;

import com.phoebus.appdemowallet.models.ResponseCredentials;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface CredentialService {
    String BASE_URL = "http://177.43.232.243:9093/auth/realms/wallet/protocol/openid-connect/";//TODO mudar para url externa

    @FormUrlEncoded
    @POST("token")
    Call<ResponseCredentials> token(
            @Field("client_id") String clientId,
            @Field("client_secret") String clientSecret,
            @Field("grant_type") String grantType
    );
}
