using System;
using System.IO;
using System.Windows;
using System.Windows.Controls;
using System.Drawing;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using Microsoft.Win32;
using eBdb.EpubReader;
using Renci.SshNet;
using SHDocVw;

namespace ContentProvider
{
    /// <summary>
    /// Interaction logic for MainWindow.xaml
    /// </summary>
    public partial class MainWindow : Window
    {
        static int count = 0;
        Epub epub = null;
        string fileFullName = null;
        //string title;
        //string plainText;
        //string htmlText;
        ContentData contentData;
        System.Collections.Stack addedRedo = new System.Collections.Stack();
        System.Collections.Stack removedRedo= new System.Collections.Stack();
        System.Collections.Stack addedUndo = new System.Collections.Stack();
        System.Collections.Stack removedUndo = new System.Collections.Stack();
        private bool handle = true;
        string eraserToUse = null;

        public MainWindow()
        {
            InitializeComponent();

            this.myInkCanvas.EditingMode = InkCanvasEditingMode.Ink;
            myInkCanvas.Strokes.StrokesChanged += Strokes_Changed;
            
            button_back.IsEnabled = false;
            button_front.IsEnabled = false;

            // url : http://susemi99.kr/1651
            string searchName = "hello";
            string searchURL = "https://apis.daum.net/search/image?apikey=a509068e1960142d8d73fa4240a49ae1&q=" + searchName + "&output=json";


            //MessageBox.Show(searchURL);
        }

        private void btnBackShowDlg_Click(object sender, RoutedEventArgs e)
        {
            paraBodyText.Inlines.Clear();
            try
            {
                contentData = epub.Content[--count] as ContentData;
            }
            catch(ArgumentOutOfRangeException)
            {
                MessageBox.Show("Its first page!");
                count = 0;
                contentData = epub.Content[count] as ContentData;
            }
            Run str = new Run(contentData.GetContentAsPlainText());
            paraBodyText.Inlines.Add(str);
        }

        private void btnFrontShowDlg_Click(object sender, RoutedEventArgs e)
        {
            paraBodyText.Inlines.Clear();
            try
            {
                contentData = epub.Content[++count] as ContentData;
            }
            catch(ArgumentOutOfRangeException ex)
            {
                MessageBox.Show("Its last page!");
                count--;
                contentData = epub.Content[count] as ContentData;
            }
            
            Run str = new Run(contentData.GetContentAsPlainText());
            paraBodyText.Inlines.Add(str);
        }

        public string ShowFileOpenDialog()
        {
            OpenFileDialog openFileDialog = new OpenFileDialog();
            openFileDialog.Title = "File open";
            openFileDialog.Filter = "e-book (*.epub) | *.epub";

            if(openFileDialog.ShowDialog() == true)
            {
                button_back.IsEnabled = true;
                button_front.IsEnabled = true;

                return openFileDialog.FileName;
            }

            return null;
        }

        public string ShowFileOpenDialog2()
        {
            OpenFileDialog openFileDialog = new OpenFileDialog();
            openFileDialog.Title = "File upload";
            openFileDialog.Filter = "e-book (*.epub) | *.epub";

            if (openFileDialog.ShowDialog() == true)
            {
                return openFileDialog.FileName;
            }

            return null;
        }

        public string ShowFileSaveDialog()
        {
            SaveFileDialog saveFileDialog = new SaveFileDialog();
            saveFileDialog.Filter = "e-book (*.bmp) | *.bmp";

            if(saveFileDialog.ShowDialog() == true)
            {
                //File.WriteAllBytes(saveFileDialog.FileName);
                //MessageBox.Show(saveFileDialog.FileName);
                return saveFileDialog.FileName;
            }

            return null;
        }

        public System.Drawing.Image ByteArrayToImage(byte[] b)
        {
            ImageConverter imgcvt = new ImageConverter();
            System.Drawing.Image img = (System.Drawing.Image)imgcvt.ConvertFrom(b);

            return img;
        }

        public byte[] ImageToByteArray(System.Drawing.Image img)
        {
            MemoryStream ms = new MemoryStream();
            img.Save(ms, System.Drawing.Imaging.ImageFormat.Bmp);

            return ms.ToArray();
        }

        private void NewCommandBinding_Executed(object sender, ExecutedRoutedEventArgs e)
        {
            epub = null;

            paraBodyText.Inlines.Clear();
            myInkCanvas.Strokes.Clear();

            button_back.IsEnabled = false;
            button_front.IsEnabled = false;
        }

        // file -> new click
        private void menuNew_Click(object sender, RoutedEventArgs e)
        {
            epub = null;

            paraBodyText.Inlines.Clear();
            myInkCanvas.Strokes.Clear();

            button_back.IsEnabled = false;
            button_front.IsEnabled = false;
        }

        // file -> open shortcut(ctrl+o)
        private void OpenCommandBinding_Executed(object sender, ExecutedRoutedEventArgs e)
        {
            fileFullName = ShowFileOpenDialog();
            if (fileFullName != null)
                epub = new Epub(fileFullName);
        }

        // file -> open click
        private void menuOpen_Click(object sender, RoutedEventArgs e)
        {
            fileFullName = ShowFileOpenDialog();
            if (fileFullName != null)
                epub = new Epub(fileFullName);
        }

        // file -> save shortcut(ctrl+s)
        private void SaveCommandBinding_Executed(object sender, ExecutedRoutedEventArgs e)
        {
            System.Drawing.Image img;
            string saveFileName = ShowFileSaveDialog();

            if (saveFileName != null)
            {
                img = ByteArrayToImage(CanvasToBitmapBytes());
                img.Save(saveFileName, System.Drawing.Imaging.ImageFormat.Bmp);
                img.Dispose();
            }
        }

        private byte[] CanvasToBitmapBytes()
        {
            int margin = (int)this.myInkCanvas.Margin.Left;
            int width = (int)this.myInkCanvas.ActualWidth - margin;
            int height = (int)this.myInkCanvas.ActualHeight - margin;
            byte[] bitmapBytes;

            RenderTargetBitmap rtb = new RenderTargetBitmap(width, height, 96d, 96d, PixelFormats.Default);
            rtb.Render(myInkCanvas);

            BmpBitmapEncoder encoder = new BmpBitmapEncoder();
            encoder.Frames.Add(BitmapFrame.Create(rtb));

            using (MemoryStream ms = new MemoryStream())
            {
                encoder.Save(ms);
                ms.Position = 0;
                bitmapBytes = ms.ToArray();
            }

            return bitmapBytes;
        }

        // file -> save click
        private void menuSave_Click(object sender, RoutedEventArgs e)
        {
            System.Drawing.Image img;
            string saveFileName = ShowFileSaveDialog();

            if (saveFileName != null)
            {
                img = ByteArrayToImage(CanvasToBitmapBytes());
                img.Save(saveFileName, System.Drawing.Imaging.ImageFormat.Bmp);
                img.Dispose();
            }
        }

        private void menuUpload_Click(object sender, RoutedEventArgs e)
        {
            string uploadfile = null;

            FileUploadSTFP(ref uploadfile);

            uploadfile = Path.GetFileName(uploadfile);

            var ie = new InternetExplorer();
            var webBrowser = (IWebBrowserApp)ie;
            webBrowser.Visible = false;
            webBrowser.Navigate("http://computer.kevincrack.com/epub_upload.jsp?name=" + uploadfile);
        }

        public void FileUploadSTFP(ref string uploadfile)
        {
            const string host = ;
            const int port = ;
            const string username = ;
            const string password = ;
            const string workingdirectory = "/root";

            uploadfile = ShowFileOpenDialog2();

            using (var client = new SftpClient(host, port, username, password))
            {
                client.Connect();

                client.ChangeDirectory(workingdirectory);

                var listDirectory = client.ListDirectory(workingdirectory);

                foreach (var fi in listDirectory) ;

                using (var fileStream = new FileStream(uploadfile, FileMode.Open))
                {
                    client.UploadFile(fileStream, Path.GetFileName(uploadfile));
                }
            }
        }

        private void menuDownload_Click(object sender, RoutedEventArgs e)
        {
            FileChoiceWindow fcw = new FileChoiceWindow();
            fcw.Owner = this;
            fcw.Show();
        }

        private void Strokes_Changed(object sender, System.Windows.Ink.StrokeCollectionChangedEventArgs e)
        {
            if (handle)
            {
                addedRedo.Push((System.Windows.Ink.StrokeCollection)e.Added);
                removedRedo.Push((System.Windows.Ink.StrokeCollection)e.Removed);
            }
        }

        // edit -> redo shortcut(ctrl+z)
        private void RedoCommandBinding_Executed(object sender, ExecutedRoutedEventArgs e)
        {
            handle = false;
            if (addedRedo.Count > 0)
            {
                myInkCanvas.Strokes.Remove((System.Windows.Ink.StrokeCollection)addedRedo.Peek());
                addedUndo.Push(addedRedo.Pop());
            }
            if (removedRedo.Count > 0)
            {
                myInkCanvas.Strokes.Add((System.Windows.Ink.StrokeCollection)removedRedo.Peek());
                removedUndo.Push(removedRedo.Pop());
            }
            handle = true;
        }

        // edit -> redo click
        private void menuRedo_Click(object sender, RoutedEventArgs e)
        {
            handle = false;
            if (addedRedo.Count > 0)
            {
                myInkCanvas.Strokes.Remove((System.Windows.Ink.StrokeCollection)addedRedo.Peek());
                addedUndo.Push(addedRedo.Pop());
            }
            if (removedRedo.Count > 0)
            {
                myInkCanvas.Strokes.Add((System.Windows.Ink.StrokeCollection)removedRedo.Peek());
                removedUndo.Push(removedRedo.Pop());
            }
            handle = true;
        }

        // edit -> undo shortcut(ctrl+y)
        private void UndoCommandBinding_Executed(object sender, ExecutedRoutedEventArgs e)
        {
            handle = false;
            if (addedUndo.Count > 0)
            {
                myInkCanvas.Strokes.Add((System.Windows.Ink.StrokeCollection)addedUndo.Peek());
                addedRedo.Push(addedUndo.Pop());
            }
            if (removedUndo.Count > 0)
            {
                myInkCanvas.Strokes.Remove((System.Windows.Ink.StrokeCollection)removedUndo.Peek());
                removedRedo.Push(removedUndo.Pop());
            }
            handle = true;
        }

        // edit -> undo click
        private void menuUndo_Click(object sender, RoutedEventArgs e)
        {
            handle = false;
            if (addedUndo.Count > 0)
            {
                myInkCanvas.Strokes.Add((System.Windows.Ink.StrokeCollection)addedUndo.Peek());
                addedRedo.Push(addedUndo.Pop());
            }
            if (removedUndo.Count > 0)
            {
                myInkCanvas.Strokes.Remove((System.Windows.Ink.StrokeCollection)removedUndo.Peek());
                removedRedo.Push(removedUndo.Pop());
            }
            handle = true;
        }

        private void btnPencil_Click(object sender, RoutedEventArgs e)
        {
            this.myInkCanvas.EditingMode = InkCanvasEditingMode.Ink;
        }

        private void btnEraser_Click(object sender, RoutedEventArgs e)
        {
            switch(eraserToUse)
            {
                case "Stroke Eraser":
                    this.myInkCanvas.EditingMode = InkCanvasEditingMode.EraseByStroke;
                    break;

                case "Point Eraser":
                    this.myInkCanvas.EditingMode = InkCanvasEditingMode.EraseByPoint;
                    break;
            }
        }

        private void eraser_Choice(object sender, RoutedEventArgs e)
        {
            var button = sender as RadioButton;

            eraserToUse = button.Content.ToString();

            switch (eraserToUse)
            {
                case "Stroke Eraser":
                    this.myInkCanvas.EditingMode = InkCanvasEditingMode.EraseByStroke;
                    break;

                case "Point Eraser":
                    this.myInkCanvas.EditingMode = InkCanvasEditingMode.EraseByPoint;
                    break;
            }
        }

        private void btnChoice_Click(object sender, RoutedEventArgs e)
        {
            this.myInkCanvas.EditingMode = InkCanvasEditingMode.Select;
        }

        private void imageFind_Click(object sender, RoutedEventArgs e)
        {
            imageFind iF = new imageFind();
            iF.Owner = this;
            iF.Show();
        }
    }
}
