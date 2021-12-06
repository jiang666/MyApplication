package cn.face.sdk;



public class FaceInfo {
	public FaceInfo()
	{

	}

	public int detected; // 0：跟踪到的人脸; 1: 检测到的人脸; 2:检测到但不会被进行后续计算(关键点)的人脸
	                     // 注： 跟踪到的仅ID和人脸框数据有效

	public int trackId;  // 人脸ID（ID<0表示没有进入跟踪）
	
	// Face rect人脸框
	public int x;        // 左上角x坐标
	public int y;        // 坐上角y坐标
	public int width;    // 人脸宽
	public int height;   // 人脸高
	
	// face_point关键点，最多68个关键点，目前使用9点关键点模型
	public float[] keypt_x;      // 关键点x坐标
	public float[] keypt_y;      // 关键点y坐标
	public float keyptScore;     // 关键点得分
	
	// face_aligned人脸对齐数据，用于提特征
	public byte[] alignedData;  // 图像数据，空间分配128*128
	public int alignedW;        // 宽
	public int alignedH;        // 高
	public int nChannels;       // 图像通道
	
	// head_pose头部姿态
	public float pitch;         // 抬头、低头,范围-90到90，越大表示越抬头
	public float yaw;           // 左右转头
	public float roll;          // 平面内偏头
	
	// face_quality人脸质量分
	public int   errcode;		// 质量分析错误码
	public float[] scores;      // 质量分分数项，具体含义（根据数据下标顺序）:
	/* 0 - 人脸质量总分，0.65-1.0
	* 1 - 光照分，越大表示光照越适合，0.4
	* 2 - 对称分，越大表示光照越对称，0.5
	* 3 - 正脸分，越大表示脸越正，0.65
	* 4 - 清晰度分，越大表示越清晰，0.65
	* 5 - 是否带眼镜分数，越大表示越可能没带眼镜，0.5
	* 6 - 闭嘴分数， 越大表示越可能是闭嘴，0.5
	* 7 - 左眼睁眼分数， 越大表示左眼越可能是睁眼，0.5
	* 8 - 右眼睁眼分数， 越大表示右眼越可能是睁眼，0.5-1.0
	* 9 - 肤色接近真人肤色程度，越大表示越真实，推荐范围0.5-1.0
	* 10 - 戴墨镜的置信分，越大表示戴墨镜的可能性越大，0.0-0.5
	* 11 - 眼睛被遮挡的置信度，越大表示眼镜越可能被遮挡，0.0-0.5
	* 12~31 - 备用
    */
}
