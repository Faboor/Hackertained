

public class AlphaChanger {

  final TransparentImage image;

  public AlphaChanger(TransparentImage image) {
    this.image = image;
  }

  public void change(float scale, float scaleMax) {
    image.setAlpha(Math.max(-1, Math.min(1, scale  / scaleMax)) / 2.0f);
  }
}
