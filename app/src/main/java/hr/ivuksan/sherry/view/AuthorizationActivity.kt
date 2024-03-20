package hr.ivuksan.sherry.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse
import org.json.JSONObject
import java.security.MessageDigest
import java.security.SecureRandom

class AuthorizationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val assetsManager = applicationContext.assets
        val inputStream = assetsManager.open("configPKCE.json")
        val size = inputStream.available()
        val buffer = ByteArray(size)
        inputStream.read(buffer)
        inputStream.close()
        val jsonContent = String(buffer, Charsets.UTF_8)
        val jsonObject = JSONObject(jsonContent)

        val clientId = jsonObject.getString("client_id")
        val redirectUri = jsonObject.getString("redirect_uri")
        val codeVerifier = getCodeVerifier()

        val builder = AuthorizationRequest.Builder(
            clientId,
            AuthorizationResponse.Type.TOKEN,
            redirectUri
        )
            .setShowDialog(false)
            .setScopes(arrayOf(
                "user-read-playback-state",
                "user-read-playback-position",
                "user-read-recently-played",
                "user-read-currently-playing",
                "user-read-private",
                "user-read-email",
                "user-library-read",
                "user-modify-playback-state",
                "user-follow-read",
                "user-top-read",
                "app-remote-control",
                "playlist-read-collaborative",
                "playlist-read-private",
                "playlist-modify-public",
                "playlist-modify-private")
            )
            .setCustomParam("code_challenge_method", "S256")
            .setCustomParam("code_challenge", getCodeChallenge(codeVerifier))
            .build()

        val intent = AuthorizationClient.createLoginActivityIntent(this, builder)
        authorizationResultLauncher.launch(intent)
    }

    private val authorizationResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val response = AuthorizationClient.getResponse(result.resultCode, result.data)

        when (response.type) {
            AuthorizationResponse.Type.TOKEN -> {
                val sharedPreferences = getSharedPreferences("AuthorizationPreferences", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putString("access_token", response.accessToken)
                editor.apply()
                val hIntent = Intent(applicationContext, HomeActivity::class.java)
                startActivity(hIntent)
                finish()
            }
            AuthorizationResponse.Type.ERROR -> {
                Log.e("AuthorizationActivity", response.error)
            }
            else -> {
                Log.e("AuthorizationActivity", response.error)
            }
        }
    }

    private fun getCodeVerifier(): String {
        val secureRandom = SecureRandom()
        val code = ByteArray(64)
        secureRandom.nextBytes(code)
        return Base64.encodeToString(
            code,
            Base64.URL_SAFE or Base64.NO_WRAP or Base64.NO_PADDING
        )
    }

    private fun getCodeChallenge(verifier: String): String {
        val bytes = verifier.toByteArray()
        val messageDigest = MessageDigest.getInstance("SHA-256")
        messageDigest.update(bytes, 0, bytes.size)
        val digest = messageDigest.digest()
        return Base64.encodeToString(
            digest,
            Base64.URL_SAFE or Base64.NO_WRAP or Base64.NO_PADDING
        )
    }
}