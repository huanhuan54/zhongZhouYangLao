from pathlib import Path

from PIL import Image, ImageDraw, ImageFont


ROOT = Path(__file__).resolve().parents[2]
OUTPUT = ROOT / "homework-output" / "screenshots" / "finance-design-comparison.png"

PAIRS = [
    (
        "Bill management",
        Path(r"C:\Users\青峰不语\AppData\Local\Temp\codex-clipboard-65f11f94-4cc6-4e03-b1f9-ba172b73f246.png"),
        ROOT / "homework-output" / "screenshots" / "defense-finance-bill.png",
        0.58,
    ),
    (
        "Bill detail",
        Path(r"C:\Users\青峰不语\AppData\Local\Temp\codex-clipboard-13eb29d9-e89a-4197-8af3-7bd891dbb887.png"),
        ROOT / "homework-output" / "screenshots" / "defense-finance-bill-detail.png",
        0.59,
    ),
    (
        "Prepayment",
        Path(r"C:\Users\青峰不语\AppData\Local\Temp\codex-clipboard-bec89345-43c2-404c-90ed-d587f980aed0.png"),
        ROOT / "homework-output" / "screenshots" / "defense-finance-prepayment.png",
        0.59,
    ),
    (
        "Balance query",
        Path(r"C:\Users\青峰不语\AppData\Local\Temp\codex-clipboard-d7a02cbe-f24e-4918-8acd-19f253117fce.png"),
        ROOT / "homework-output" / "screenshots" / "defense-finance-balance.png",
        0.59,
    ),
    (
        "Arrears",
        Path(r"C:\Users\青峰不语\AppData\Local\Temp\codex-clipboard-475f7d16-f64d-4ad6-a031-74b4e5f1e69a.png"),
        ROOT / "homework-output" / "screenshots" / "defense-finance-arrears.png",
        0.59,
    ),
]

PANEL_WIDTH = 700
PANEL_HEIGHT = 438
TITLE_HEIGHT = 42
ROW_GAP = 24
OUTER_PAD = 24


def fit(image: Image.Image, width: int, height: int) -> Image.Image:
    image = image.copy()
    image.thumbnail((width, height), Image.Resampling.LANCZOS)
    return image


def paste_center(canvas: Image.Image, image: Image.Image, box: tuple[int, int, int, int]) -> None:
    left, top, right, bottom = box
    x = left + (right - left - image.width) // 2
    y = top + (bottom - top - image.height) // 2
    canvas.paste(image, (x, y))


def main() -> None:
    width = OUTER_PAD * 2 + PANEL_WIDTH * 2 + 20
    row_height = TITLE_HEIGHT + PANEL_HEIGHT
    height = OUTER_PAD * 2 + len(PAIRS) * row_height + (len(PAIRS) - 1) * ROW_GAP
    canvas = Image.new("RGB", (width, height), "#eef1f5")
    draw = ImageDraw.Draw(canvas)
    font = ImageFont.load_default()

    for index, (title, reference_path, implementation_path, crop_ratio) in enumerate(PAIRS):
        y = OUTER_PAD + index * (row_height + ROW_GAP)
        draw.text((OUTER_PAD, y + 10), f"{title} - prototype", fill="#263445", font=font)
        draw.text(
            (OUTER_PAD + PANEL_WIDTH + 20, y + 10),
            f"{title} - implementation",
            fill="#263445",
            font=font,
        )

        reference = Image.open(reference_path).convert("RGB")
        reference = reference.crop((0, 0, int(reference.width * crop_ratio), reference.height))
        implementation = Image.open(implementation_path).convert("RGB")

        left_box = (OUTER_PAD, y + TITLE_HEIGHT, OUTER_PAD + PANEL_WIDTH, y + row_height)
        right_box = (
            OUTER_PAD + PANEL_WIDTH + 20,
            y + TITLE_HEIGHT,
            OUTER_PAD + PANEL_WIDTH * 2 + 20,
            y + row_height,
        )
        draw.rectangle(left_box, fill="white", outline="#c8cfd8")
        draw.rectangle(right_box, fill="white", outline="#c8cfd8")
        paste_center(canvas, fit(reference, PANEL_WIDTH - 8, PANEL_HEIGHT - 8), left_box)
        paste_center(canvas, fit(implementation, PANEL_WIDTH - 8, PANEL_HEIGHT - 8), right_box)

    OUTPUT.parent.mkdir(parents=True, exist_ok=True)
    canvas.save(OUTPUT, quality=94)
    print(OUTPUT)


if __name__ == "__main__":
    main()
