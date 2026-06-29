from pathlib import Path

from PIL import Image, ImageDraw


source_dir = Path(
    r"C:\Users\青峰不语\.codex\attachments\19119822-ca9a-474b-886f-05d1c169b318"
)
output_file = Path(
    r"C:\Users\青峰不语\Desktop\黑马实训\zzyl-springboot\RuoYi-Springboot3"
    r"\homework-output\screenshots\operations-reference-contact-sheet.png"
)

files = [source_dir / f"image-{index}.png" for index in range(1, 27)]
thumb_width, thumb_height = 320, 210
columns = 4
rows = (len(files) + columns - 1) // columns

canvas = Image.new(
    "RGB",
    (columns * thumb_width, rows * (thumb_height + 28)),
    "white",
)
draw = ImageDraw.Draw(canvas)

for index, file in enumerate(files):
    image = Image.open(file).convert("RGB")
    image.thumbnail((thumb_width - 12, thumb_height - 12))
    column = index % columns
    row = index // columns
    x = column * thumb_width + (thumb_width - image.width) // 2
    row_top = row * (thumb_height + 28)
    y = row_top + (thumb_height - image.height) // 2
    canvas.paste(image, (x, y))
    draw.text(
        (column * thumb_width + 8, row_top + thumb_height + 4),
        f"image-{index + 1}.png",
        fill="black",
    )

canvas.save(output_file)
print(output_file)
