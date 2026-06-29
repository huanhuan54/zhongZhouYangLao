from pathlib import Path

from PIL import Image, ImageDraw


source_dir = Path(
    r"C:\Users\青峰不语\.codex\attachments\19119822-ca9a-474b-886f-05d1c169b318"
)
screen_dir = Path(
    r"C:\Users\青峰不语\Desktop\黑马实训\zzyl-springboot\RuoYi-Springboot3"
    r"\homework-output\screenshots"
)
output_file = screen_dir / "operations-design-comparison.png"

pairs = [
    ("健康评估详情", source_dir / "image-2.png", screen_dir / "defense-health-detail.png", 0.60),
    ("入住申请表单", source_dir / "image-6.png", screen_dir / "defense-admission-form.png", 1.00),
    ("智能床位", source_dir / "image-14.png", screen_dir / "defense-smart-bed.png", 0.60),
    ("设备详情", source_dir / "image-21.png", screen_dir / "defense-device-detail.png", 0.60),
    ("报警数据", source_dir / "image-26.png", screen_dir / "defense-alarm-data.png", 0.60),
]

cell_width, cell_height = 680, 410
label_height = 34
canvas = Image.new(
    "RGB",
    (cell_width * 2, (cell_height + label_height) * len(pairs)),
    "#f2f4f7",
)
draw = ImageDraw.Draw(canvas)


def fit(image: Image.Image) -> Image.Image:
    image.thumbnail((cell_width - 24, cell_height - 24))
    return image


for row, (title, source_file, screen_file, source_ratio) in enumerate(pairs):
    source = Image.open(source_file).convert("RGB")
    if source_ratio < 1:
        source = source.crop((0, 0, int(source.width * source_ratio), source.height))
    implementation = Image.open(screen_file).convert("RGB")
    source = fit(source)
    implementation = fit(implementation)
    top = row * (cell_height + label_height)
    draw.text((12, top + 8), f"{title} - 原型", fill="#172033")
    draw.text((cell_width + 12, top + 8), f"{title} - 实现", fill="#172033")
    source_x = (cell_width - source.width) // 2
    implementation_x = cell_width + (cell_width - implementation.width) // 2
    image_y = top + label_height + (cell_height - source.height) // 2
    implementation_y = top + label_height + (cell_height - implementation.height) // 2
    canvas.paste(source, (source_x, image_y))
    canvas.paste(implementation, (implementation_x, implementation_y))

canvas.save(output_file)
print(output_file)
