using UnityEngine;
using System.Collections;

[ExecuteInEditMode]
[AddComponentMenu("Image Effects/Blur/Blur")]
public class BlurOnCamera : MonoBehaviour {
	/// Blur iterations - larger number means more blur.
	public int iterations = 1;
	
	/// Blur spread for each iteration. Lower values
	/// give better looking blur, but require more iterations to
	/// get large blurs. Value is usually between 0.5 and 1.0.
	public float blurSpread = 0.9f;
	public Shader blurShader = null;

	public float value = 0.5f;

	static Material m_Material = null;
	protected Material material {
		get {
			if (m_Material == null) {
				m_Material = new Material(blurShader);
				m_Material.hideFlags = HideFlags.DontSave;
			}
			return m_Material;
		} 
	}

	protected void OnDisable() {
		if( m_Material ) {
			DestroyImmediate( m_Material );
		}
	}

	// Use this for initialization
	protected void Start()
	{
		// Disable if we don't support image effects
		if (!SystemInfo.supportsImageEffects) {
			enabled = false;
			return;
		}
		// Disable if the shader can't run on the users graphics card
		if (!blurShader || !material.shader.isSupported) {
			enabled = false;
			return;
		}
	}

	public void FourTapCone (RenderTexture source, RenderTexture dest, int iteration)
	{
		//float off = 0.5f + iteration*blurSpread;
//		GameObject player = GameObject.Find("Player");
//		float off = player.GetComponent<EyeManager>().lrDistance/8.0f;
//		GameObject mainCamera = GameObject.Find("Blur Camera");
//		if (player.GetComponent<EyeManager>().lrDistance>=0.0f&&player.GetComponent<EyeManager>().lrDistance<=0.02f){
//			mainCamera.GetComponent<BlurOnCamera>().blurSpread = 0.0f;
//			mainCamera.GetComponent<BlurOnCamera>().enabled = false;
//		}else{
//			mainCamera.GetComponent<BlurOnCamera>().enabled = true;
//		}
		//float off = iteration*blurSpread;
		float off = value;
		Graphics.BlitMultiTap (source, dest, material,
		                       new Vector2(-off, -off),
		                       new Vector2(-off,  off),
		                       new Vector2( off,  off),
		                       new Vector2( off, -off)
		                       );
	}

	// Downsamples the texture to a quarter resolution.
	private void DownSample4x (RenderTexture source, RenderTexture dest)
	{
		float off = 0.01f;
		Graphics.BlitMultiTap (source, dest, material,
		                       new Vector2(-off, -off),
		                       new Vector2(-off,  off),
		                       new Vector2( off,  off),
		                       new Vector2( off, -off)
		                       );
	}

	// Called by the camera to apply the image effect
	void OnRenderImage (RenderTexture source, RenderTexture destination) {
		int rtW = source.width/4;
		int rtH = source.height/4;
		RenderTexture buffer = RenderTexture.GetTemporary(rtW, rtH, 0);
		
		// Copy source to the 4x4 smaller texture.
		DownSample4x (source, buffer);
			
		// Blur the small texture
		for(int i = 0; i < iterations; i++)
		{
			RenderTexture buffer2 = RenderTexture.GetTemporary(rtW, rtH, 0);
			FourTapCone (buffer, buffer2, i);
			RenderTexture.ReleaseTemporary(buffer);
			buffer = buffer2;
		}
		Graphics.Blit(buffer, destination);
			
		RenderTexture.ReleaseTemporary(buffer);
	}
	
	// Update is called once per frame
	void Update () {

	}
}
